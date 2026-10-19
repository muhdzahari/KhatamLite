package com.example.domain

import com.example.data.local.KhatamPlanEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.PacingMode
import com.example.data.model.ScheduleSlot
import com.example.data.quran.SurahRegistry
import java.util.Calendar
import kotlin.math.ceil
import kotlin.math.roundToInt

data class PacingSummary(
    val dailyTargetVerses: Int,
    val activeWakingHours: Int,
    val sleepingHours: Int,
    val versesPerSlotDisplay: String,
    val totalSlotsToday: Int,
    val completedSlotsToday: Int,
    val percentCompletedToday: Float,
    val overallPercentCompleted: Float,
    val projectedKhatamDateString: String,
    val daysRemaining: Int,
    val paceStatus: String
)

object PacingCalculator {

    fun calculateDailyTarget(targetDays: Int, totalVerses: Int = SurahRegistry.TOTAL_QURAN_VERSES): Int {
        val safeDays = targetDays.coerceAtLeast(1)
        return ceil(totalVerses.toDouble() / safeDays).toInt().coerceAtLeast(1)
    }

    fun calculateWakingHours(startHour: Int, endHour: Int): Int {
        return if (endHour > startHour) {
            endHour - startHour
        } else {
            (24 - startHour) + endHour
        }.coerceIn(4, 24)
    }

    fun generateDailySchedule(
        plan: KhatamPlanEntity,
        progress: UserProgressEntity,
        todayCompletedVerses: Int,
        currentHourOfDay: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    ): List<ScheduleSlot> {
        val dailyTarget = calculateDailyTarget(plan.targetDays, plan.totalVersesToRead)
        val wakingHours = calculateWakingHours(plan.wakingHourStart, plan.wakingHourEnd)
        val mode = try {
            PacingMode.valueOf(plan.pacingMode)
        } catch (_: Exception) {
            PacingMode.HOURLY
        }

        // Today's base starting verse for the daily allocation
        // We anchor today's target window to the user's progress or the planned daily chunk
        val startOfTodayGlobal = progress.currentGlobalVerseIndex

        val slotTimes = when (mode) {
            PacingMode.HOURLY -> {
                (0 until wakingHours).map { i ->
                    val hour = (plan.wakingHourStart + i) % 24
                    SlotTime(
                        hour = hour,
                        minute = 0,
                        label = formatHour(hour),
                        timeDisplay = formatTime(hour, 0)
                    )
                }
            }
            PacingMode.EVERY_2_HOURS -> {
                val count = (wakingHours / 2).coerceAtLeast(1)
                (0 until count).map { i ->
                    val hour = (plan.wakingHourStart + (i * 2)) % 24
                    SlotTime(
                        hour = hour,
                        minute = 0,
                        label = "${formatHour(hour)} Block",
                        timeDisplay = formatTime(hour, 0)
                    )
                }
            }
            PacingMode.SALAH_5_TIMES -> {
                listOf(
                    SlotTime(plan.wakingHourStart, 0, "After Fajr", formatTime(plan.wakingHourStart, 0)),
                    SlotTime(12, 30, "After Dhuhr", "12:30 PM"),
                    SlotTime(15, 45, "After Asr", "03:45 PM"),
                    SlotTime(18, 15, "After Maghrib", "06:15 PM"),
                    SlotTime(19, 45, "After Isha", "07:45 PM")
                )
            }
            PacingMode.TWICE_DAILY -> {
                val morningHour = (plan.wakingHourStart + 1).coerceAtMost(11)
                val eveningHour = (plan.wakingHourEnd - 2).coerceAtLeast(16)
                listOf(
                    SlotTime(morningHour, 0, "Morning Session", formatTime(morningHour, 0)),
                    SlotTime(eveningHour, 0, "Evening Session", formatTime(eveningHour, 0))
                )
            }
        }

        val totalSlots = slotTimes.size
        // Distribute `dailyTarget` verses across `totalSlots`
        val basePerSlot = dailyTarget / totalSlots
        val remainder = dailyTarget % totalSlots

        var runningVersePointer = startOfTodayGlobal
        var foundCurrent = false

        return slotTimes.mapIndexed { index, slotTime ->
            val slotVerseCount = basePerSlot + if (index < remainder) 1 else 0
            val startVerse = runningVersePointer
            val endVerse = if (slotVerseCount > 0) {
                (startVerse + slotVerseCount - 1).coerceAtMost(SurahRegistry.TOTAL_QURAN_VERSES)
            } else {
                startVerse
            }
            if (slotVerseCount > 0) {
                runningVersePointer = endVerse + 1
            }

            val requiredCumulativeForSlot = (index + 1) * (dailyTarget.toFloat() / totalSlots)
            val isCompleted = if (slotVerseCount == 0) true else {
                todayCompletedVerses >= requiredCumulativeForSlot.roundToInt() ||
                        progress.currentGlobalVerseIndex > endVerse
            }

            val isCurrent = !foundCurrent && (!isCompleted || currentHourOfDay <= slotTime.hour)
            if (isCurrent && slotVerseCount > 0) foundCurrent = true

            ScheduleSlot(
                id = index + 1,
                label = slotTime.label,
                timeDisplay = slotTime.timeDisplay,
                startGlobalVerse = startVerse,
                endGlobalVerse = endVerse,
                versesCount = slotVerseCount,
                surahAyahRange = if (slotVerseCount > 0) SurahRegistry.formatVerseRange(startVerse, endVerse) else "Reflective Pause",
                isCompleted = isCompleted,
                isCurrent = isCurrent
            )
        }
    }

    fun computeSummary(
        plan: KhatamPlanEntity,
        progress: UserProgressEntity,
        todayCompletedVerses: Int,
        schedule: List<ScheduleSlot>
    ): PacingSummary {
        val dailyTarget = calculateDailyTarget(plan.targetDays, plan.totalVersesToRead)
        val wakingHours = calculateWakingHours(plan.wakingHourStart, plan.wakingHourEnd)
        val sleepingHours = 24 - wakingHours
        val completedSlots = schedule.count { it.isCompleted }
        val totalSlots = schedule.size

        val versesPerSlotDisplay = when {
            totalSlots > 0 && dailyTarget > 0 -> {
                val avg = dailyTarget.toDouble() / totalSlots
                if (avg < 1.0) "~1 verse every ${ceil(1.0 / avg).toInt()} hrs"
                else String.format("%.1f verses / slot", avg)
            }
            else -> "$dailyTarget verses / day"
        }

        val todayPercent = if (dailyTarget > 0) {
            (todayCompletedVerses.toFloat() / dailyTarget).coerceIn(0f, 1f)
        } else 0f

        val overallPercent = (progress.totalVersesCompleted.toFloat() / SurahRegistry.TOTAL_QURAN_VERSES).coerceIn(0f, 1f)

        // Calculate days remaining & projected completion
        val remainingVerses = (SurahRegistry.TOTAL_QURAN_VERSES - progress.totalVersesCompleted).coerceAtLeast(0)
        val daysElapsed = ((System.currentTimeMillis() - plan.startDateEpoch) / (1000L * 60 * 60 * 24)).toInt().coerceAtLeast(1)
        val avgDailyRead = if (daysElapsed > 0) {
            (progress.totalVersesCompleted.toDouble() / daysElapsed).coerceAtLeast(1.0)
        } else {
            dailyTarget.toDouble()
        }

        val projectedRemainingDays = ceil(remainingVerses / avgDailyRead).toInt()
        val projectedEpoch = System.currentTimeMillis() + (projectedRemainingDays.toLong() * 24 * 60 * 60 * 1000)
        val projectedCal = Calendar.getInstance().apply { timeInMillis = projectedEpoch }
        val projectedDateStr = String.format(
            "%tB %te, %tY",
            projectedCal, projectedCal, projectedCal
        )

        val targetCal = Calendar.getInstance().apply { timeInMillis = plan.targetEndDateEpoch }
        val daysRemainingTarget = ((plan.targetEndDateEpoch - System.currentTimeMillis()) / (1000L * 60 * 60 * 24)).toInt().coerceAtLeast(0)

        val paceStatus = when {
            projectedRemainingDays <= daysRemainingTarget - 5 -> "Ahead of Schedule ⚡"
            projectedRemainingDays <= daysRemainingTarget + 5 -> "On Track 🌟"
            else -> "Behind Schedule 📖"
        }

        return PacingSummary(
            dailyTargetVerses = dailyTarget,
            activeWakingHours = wakingHours,
            sleepingHours = sleepingHours,
            versesPerSlotDisplay = versesPerSlotDisplay,
            totalSlotsToday = totalSlots,
            completedSlotsToday = completedSlots,
            percentCompletedToday = todayPercent,
            overallPercentCompleted = overallPercent,
            projectedKhatamDateString = projectedDateStr,
            daysRemaining = daysRemainingTarget,
            paceStatus = paceStatus
        )
    }

    private data class SlotTime(
        val hour: Int,
        val minute: Int,
        val label: String,
        val timeDisplay: String
    )

    private fun formatHour(hour: Int): String {
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val ampm = if (hour < 12) "AM" else "PM"
        return String.format("%02d:00 %s", h, ampm)
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val ampm = if (hour < 12) "AM" else "PM"
        return String.format("%02d:%02d %s", h, minute, ampm)
    }
}

package com.example

import com.example.data.local.KhatamPlanEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.PacingMode
import com.example.data.quran.SurahRegistry
import com.example.domain.PacingCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuranPacingTest {

    @Test
    fun testSurahRegistryCompleteness() {
        assertEquals(114, SurahRegistry.surahs.size)
        val total = SurahRegistry.surahs.sumOf { it.totalVerses }
        assertEquals(6236, total)
        assertEquals(6236, SurahRegistry.TOTAL_QURAN_VERSES)
    }

    @Test
    fun testGlobalVerseIndexMapping() {
        val (firstSurah, firstAyah) = SurahRegistry.getSurahAndAyahFromGlobalIndex(1)
        assertEquals(1, firstSurah.number)
        assertEquals("Al-Fatihah", firstSurah.nameEnglish)
        assertEquals(1, firstAyah)

        val (baqarahSurah, baqarahAyah) = SurahRegistry.getSurahAndAyahFromGlobalIndex(8)
        assertEquals(2, baqarahSurah.number)
        assertEquals(1, baqarahAyah)

        val (lastSurah, lastAyah) = SurahRegistry.getSurahAndAyahFromGlobalIndex(6236)
        assertEquals(114, lastSurah.number)
        assertEquals("An-Nas", lastSurah.nameEnglish)
        assertEquals(6, lastAyah)
    }

    @Test
    fun testYearKhatamPacing() {
        // 365 Days target
        val dailyTarget = PacingCalculator.calculateDailyTarget(365, 6236)
        // 6236 / 365 = 17.085 -> ceil is 18 verses/day
        assertEquals(18, dailyTarget)

        // 16 waking hours (6 AM to 10 PM)
        val wakingHours = PacingCalculator.calculateWakingHours(6, 22)
        assertEquals(16, wakingHours)

        val plan = KhatamPlanEntity(
            targetDays = 365,
            wakingHourStart = 6,
            wakingHourEnd = 22,
            pacingMode = PacingMode.HOURLY.name
        )
        val progress = UserProgressEntity(currentGlobalVerseIndex = 1, totalVersesCompleted = 0)

        val schedule = PacingCalculator.generateDailySchedule(
            plan = plan,
            progress = progress,
            todayCompletedVerses = 0,
            currentHourOfDay = 8
        )

        assertEquals(16, schedule.size)
        val totalVersesScheduled = schedule.sumOf { it.versesCount }
        assertEquals(18, totalVersesScheduled)

        val summary = PacingCalculator.computeSummary(plan, progress, 0, schedule)
        assertEquals(18, summary.dailyTargetVerses)
        assertEquals(16, summary.activeWakingHours)
        assertEquals(8, summary.sleepingHours)
    }

    @Test
    fun testPrayerTimesPacing() {
        val plan = KhatamPlanEntity(
            targetDays = 30, // Ramadan 30-day pace -> 208 verses/day
            wakingHourStart = 5,
            wakingHourEnd = 22,
            pacingMode = PacingMode.SALAH_5_TIMES.name
        )
        val progress = UserProgressEntity(currentGlobalVerseIndex = 1, totalVersesCompleted = 0)

        val schedule = PacingCalculator.generateDailySchedule(
            plan = plan,
            progress = progress,
            todayCompletedVerses = 0,
            currentHourOfDay = 12
        )

        assertEquals(5, schedule.size)
        val totalScheduled = schedule.sumOf { it.versesCount }
        assertEquals(208, totalScheduled)
    }

    @Test
    fun testFiveYearKhatamPacing() {
        val dailyTarget = PacingCalculator.calculateDailyTarget(1825, 6236)
        // 6236 / 1825 = 3.41 -> ceil is 4 verses/day
        assertEquals(4, dailyTarget)

        val plan = KhatamPlanEntity(
            targetDays = 1825, // 5 years
            wakingHourStart = 6,
            wakingHourEnd = 22,
            pacingMode = PacingMode.HOURLY.name
        )
        val progress = UserProgressEntity(currentGlobalVerseIndex = 1, totalVersesCompleted = 0)

        val schedule = PacingCalculator.generateDailySchedule(
            plan = plan,
            progress = progress,
            todayCompletedVerses = 0,
            currentHourOfDay = 8
        )

        assertEquals(16, schedule.size)
        val totalScheduled = schedule.sumOf { it.versesCount }
        assertEquals(4, totalScheduled)

        val summary = PacingCalculator.computeSummary(plan, progress, 0, schedule)
        assertEquals(4, summary.dailyTargetVerses)
        // 4 verses across 16 waking hours = ~1 verse every 4 hrs
        assertEquals("~1 verse every 4 hrs", summary.versesPerSlotDisplay)
    }
}

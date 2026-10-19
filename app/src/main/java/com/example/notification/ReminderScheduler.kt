package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.local.KhatamPlanEntity
import com.example.data.model.ScheduleSlot
import java.util.Calendar

object ReminderScheduler {

    fun scheduleRemindersForSchedule(
        context: Context,
        plan: KhatamPlanEntity,
        slots: List<ScheduleSlot>
    ) {
        if (!plan.notificationsEnabled) {
            cancelAllReminders(context)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        slots.forEachIndexed { index, slot ->
            val intent = Intent(context, QuranReminderReceiver::class.java).apply {
                putExtra(QuranReminderReceiver.EXTRA_SLOT_LABEL, slot.label)
                putExtra(QuranReminderReceiver.EXTRA_VERSE_RANGE, slot.surahAyahRange)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index + 100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Approximate slot hour
            val cal = Calendar.getInstance().apply {
                val hour = (plan.wakingHourStart + index).coerceAtMost(plan.wakingHourEnd)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        pendingIntent
                    )
                }
            } catch (_: SecurityException) {
                // If SCHEDULE_EXACT_ALARM permission is restricted
            }
        }
    }

    fun cancelAllReminders(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        for (i in 0..24) {
            val intent = Intent(context, QuranReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                i + 100,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    fun triggerTestNotification(context: Context, slotLabel: String = "Slot #1 (08:00 AM)", verseRange: String = "Al-Baqarah : 1–5") {
        QuranReminderReceiver.showNotification(context, slotLabel, verseRange)
    }
}

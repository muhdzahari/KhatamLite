package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class QuranReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val slotLabel = intent.getStringExtra(EXTRA_SLOT_LABEL) ?: "Scheduled Recitation"
        val verseRange = intent.getStringExtra(EXTRA_VERSE_RANGE) ?: "Your daily Quran verses"

        showNotification(context, slotLabel, verseRange)
    }

    companion object {
        const val CHANNEL_ID = "quran_pacer_reminders"
        const val NOTIFICATION_ID = 1001
        const val EXTRA_SLOT_LABEL = "extra_slot_label"
        const val EXTRA_VERSE_RANGE = "extra_verse_range"

        fun showNotification(context: Context, slotLabel: String, verseRange: String) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Quran Recitation Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Reminders to recite your planned Quran verses according to your schedule"
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            val launchIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Quran Recitation • $slotLabel")
                .setContentText("Time to recite: $verseRange. May Allah bless your efforts.")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Time to recite your paced Quran session: $verseRange. Keep your heart attached to the Book of Allah.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}

package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "khatam_plan")
data class KhatamPlanEntity(
    @PrimaryKey val id: Int = 1,
    val targetDays: Int = 365,
    val startVerseGlobalIndex: Int = 1,
    val totalVersesToRead: Int = 6236,
    val wakingHourStart: Int = 6, // 6:00 AM
    val wakingHourEnd: Int = 22,   // 10:00 PM (16 waking hours)
    val pacingMode: String = "HOURLY", // HOURLY, EVERY_2_HOURS, SALAH_5_TIMES, TWICE_DAILY
    val notificationsEnabled: Boolean = true,
    val startDateEpoch: Long = System.currentTimeMillis(),
    val targetEndDateEpoch: Long = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val id: Int = 1,
    val currentGlobalVerseIndex: Int = 1,
    val totalVersesCompleted: Int = 0,
    val currentStreakDays: Int = 1,
    val lastReadDateIso: String = "",
    val lastReadTimestamp: Long = 0L
)

@Entity(tableName = "daily_logs")
data class DailyLogEntity(
    @PrimaryKey val dateIso: String, // "YYYY-MM-DD"
    val versesReadCount: Int = 0,
    val targetVersesCount: Int = 17,
    val completedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val globalIndex: Int,
    val note: String = "",
    val createdAtEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "cached_verses")
data class CachedVerseEntity(
    @PrimaryKey val globalIndex: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val translationEnglish: String,
    val transliteration: String,
    val audioUrl: String
)

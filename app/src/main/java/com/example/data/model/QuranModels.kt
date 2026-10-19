package com.example.data.model

data class SurahInfo(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val meaningEnglish: String,
    val totalVerses: Int,
    val revelationType: String, // "Meccan" or "Medinan"
    val juzNumber: Int,
    val globalStartVerse: Int // 1-based index (1..6236)
)

data class Verse(
    val globalIndex: Int,
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val translationEnglish: String,
    val transliteration: String,
    val audioUrl: String
)

enum class PacingMode(val title: String, val description: String) {
    HOURLY("Hourly", "Divide across every waking hour"),
    EVERY_2_HOURS("Every 2 Hours", "Recite in batches every 2 waking hours"),
    SALAH_5_TIMES("5 Daily Prayers", "Recite after Fajr, Dhuhr, Asr, Maghrib, and Isha"),
    TWICE_DAILY("Morning & Evening", "Two focused sessions per day")
}

data class ScheduleSlot(
    val id: Int,
    val label: String,
    val timeDisplay: String,
    val startGlobalVerse: Int,
    val endGlobalVerse: Int,
    val versesCount: Int,
    val surahAyahRange: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

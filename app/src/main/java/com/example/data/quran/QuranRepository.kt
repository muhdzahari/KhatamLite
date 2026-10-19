package com.example.data.quran

import com.example.data.local.BookmarkEntity
import com.example.data.local.DailyLogEntity
import com.example.data.local.KhatamPlanEntity
import com.example.data.local.QuranDao
import com.example.data.local.UserProgressEntity
import com.example.data.model.PacingMode
import com.example.data.model.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class QuranRepository(private val quranDao: QuranDao) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val khatamPlan: Flow<KhatamPlanEntity> = quranDao.getKhatamPlan().map { plan ->
        plan ?: KhatamPlanEntity()
    }

    val userProgress: Flow<UserProgressEntity> = quranDao.getUserProgress().map { progress ->
        progress ?: UserProgressEntity(id = 1, currentGlobalVerseIndex = 1, totalVersesCompleted = 0)
    }

    val recentDailyLogs: Flow<List<DailyLogEntity>> = quranDao.getRecentDailyLogs()

    val bookmarks: Flow<List<BookmarkEntity>> = quranDao.getAllBookmarks()

    suspend fun initializeDefaultsIfNeeded() = withContext(Dispatchers.IO) {
        if (quranDao.getKhatamPlanOnce() == null) {
            val initialPlan = KhatamPlanEntity(
                id = 1,
                targetDays = 365,
                startVerseGlobalIndex = 1,
                totalVersesToRead = SurahRegistry.TOTAL_QURAN_VERSES,
                wakingHourStart = 6,
                wakingHourEnd = 22,
                pacingMode = PacingMode.HOURLY.name,
                notificationsEnabled = true,
                startDateEpoch = System.currentTimeMillis(),
                targetEndDateEpoch = System.currentTimeMillis() + (365L * 24 * 60 * 60 * 1000)
            )
            quranDao.insertOrUpdatePlan(initialPlan)
        }

        if (quranDao.getUserProgressOnce() == null) {
            val initialProgress = UserProgressEntity(
                id = 1,
                currentGlobalVerseIndex = 1,
                totalVersesCompleted = 0,
                currentStreakDays = 1,
                lastReadDateIso = todayIso(),
                lastReadTimestamp = System.currentTimeMillis()
            )
            quranDao.insertOrUpdateProgress(initialProgress)
        }
    }

    suspend fun savePlan(
        targetDays: Int,
        wakingHourStart: Int,
        wakingHourEnd: Int,
        pacingMode: PacingMode,
        notificationsEnabled: Boolean
    ) = withContext(Dispatchers.IO) {
        val existing = quranDao.getKhatamPlanOnce() ?: KhatamPlanEntity()
        val targetEndDateEpoch = existing.startDateEpoch + (targetDays.toLong() * 24 * 60 * 60 * 1000)

        val updated = existing.copy(
            targetDays = targetDays,
            wakingHourStart = wakingHourStart,
            wakingHourEnd = wakingHourEnd,
            pacingMode = pacingMode.name,
            notificationsEnabled = notificationsEnabled,
            targetEndDateEpoch = targetEndDateEpoch
        )
        quranDao.insertOrUpdatePlan(updated)
    }

    suspend fun markVerseRead(globalIndex: Int) = withContext(Dispatchers.IO) {
        markRangeRead(globalIndex, globalIndex)
    }

    suspend fun markRangeRead(startGlobalIndex: Int, endGlobalIndex: Int) = withContext(Dispatchers.IO) {
        val currentProgress = quranDao.getUserProgressOnce() ?: UserProgressEntity()
        val nextGlobal = (endGlobalIndex + 1).coerceAtMost(SurahRegistry.TOTAL_QURAN_VERSES + 1)
        val readCount = (endGlobalIndex - startGlobalIndex + 1).coerceAtLeast(1)

        val today = todayIso()
        val yesterday = yesterdayIso()
        val isConsecutive = currentProgress.lastReadDateIso == yesterday || currentProgress.lastReadDateIso == today
        val newStreak = when {
            currentProgress.lastReadDateIso == today -> currentProgress.currentStreakDays
            isConsecutive -> currentProgress.currentStreakDays + 1
            else -> 1
        }

        val newCompletedTotal = (currentProgress.totalVersesCompleted + readCount)
            .coerceAtMost(SurahRegistry.TOTAL_QURAN_VERSES)

        val updatedProgress = currentProgress.copy(
            currentGlobalVerseIndex = maxOf(currentProgress.currentGlobalVerseIndex, nextGlobal),
            totalVersesCompleted = newCompletedTotal,
            currentStreakDays = newStreak,
            lastReadDateIso = today,
            lastReadTimestamp = System.currentTimeMillis()
        )
        quranDao.insertOrUpdateProgress(updatedProgress)

        // Update daily log
        val existingLog = quranDao.getDailyLog(today)
        val plan = quranDao.getKhatamPlanOnce() ?: KhatamPlanEntity()
        val dailyTarget = kotlin.math.ceil(plan.totalVersesToRead.toDouble() / plan.targetDays.coerceAtLeast(1)).toInt()
        val newLog = if (existingLog != null) {
            existingLog.copy(
                versesReadCount = existingLog.versesReadCount + readCount,
                completedTimestamp = System.currentTimeMillis()
            )
        } else {
            DailyLogEntity(
                dateIso = today,
                versesReadCount = readCount,
                targetVersesCount = dailyTarget,
                completedTimestamp = System.currentTimeMillis()
            )
        }
        quranDao.insertOrUpdateDailyLog(newLog)
    }

    suspend fun resetProgress(startGlobalIndex: Int = 1) = withContext(Dispatchers.IO) {
        val existing = quranDao.getUserProgressOnce() ?: UserProgressEntity()
        val reset = existing.copy(
            currentGlobalVerseIndex = startGlobalIndex,
            totalVersesCompleted = 0,
            currentStreakDays = 1,
            lastReadDateIso = todayIso(),
            lastReadTimestamp = System.currentTimeMillis()
        )
        quranDao.insertOrUpdateProgress(reset)
    }

    suspend fun toggleBookmark(globalIndex: Int, note: String = "") = withContext(Dispatchers.IO) {
        val (surah, ayah) = SurahRegistry.getSurahAndAyahFromGlobalIndex(globalIndex)
        quranDao.deleteBookmarkByVerse(globalIndex)
        // If not already deleted (we insert new bookmark)
        quranDao.insertBookmark(
            BookmarkEntity(
                surahNumber = surah.number,
                ayahNumber = ayah,
                globalIndex = globalIndex,
                note = note,
                createdAtEpoch = System.currentTimeMillis()
            )
        )
    }

    suspend fun removeBookmark(globalIndex: Int) = withContext(Dispatchers.IO) {
        quranDao.deleteBookmarkByVerse(globalIndex)
    }

    suspend fun deleteBookmarkById(id: Long) = withContext(Dispatchers.IO) {
        quranDao.deleteBookmark(id)
    }

    fun isVerseBookmarked(globalIndex: Int): Flow<Boolean> {
        return quranDao.isVerseBookmarked(globalIndex)
    }

    fun getVerse(globalIndex: Int): Verse {
        return QuranVerseBank.getVerse(globalIndex)
    }

    fun getVersesForSurah(surahNumber: Int): List<Verse> {
        return QuranVerseBank.getVersesForSurah(surahNumber)
    }

    fun getVersesRange(startGlobal: Int, endGlobal: Int): List<Verse> {
        val start = startGlobal.coerceIn(1, SurahRegistry.TOTAL_QURAN_VERSES)
        val end = endGlobal.coerceIn(start, SurahRegistry.TOTAL_QURAN_VERSES)
        return (start..end).map { QuranVerseBank.getVerse(it) }
    }

    private fun todayIso(): String = dateFormat.format(Date())

    private fun yesterdayIso(): String {
        val cal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
        return dateFormat.format(cal.time)
    }
}

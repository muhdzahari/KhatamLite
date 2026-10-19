package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    @Query("SELECT * FROM khatam_plan WHERE id = 1")
    fun getKhatamPlan(): Flow<KhatamPlanEntity?>

    @Query("SELECT * FROM khatam_plan WHERE id = 1")
    suspend fun getKhatamPlanOnce(): KhatamPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlan(plan: KhatamPlanEntity)

    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getUserProgressOnce(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgressEntity)

    @Query("SELECT * FROM daily_logs ORDER BY dateIso DESC LIMIT 30")
    fun getRecentDailyLogs(): Flow<List<DailyLogEntity>>

    @Query("SELECT * FROM daily_logs WHERE dateIso = :dateIso")
    suspend fun getDailyLog(dateIso: String): DailyLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDailyLog(log: DailyLogEntity)

    @Query("SELECT * FROM bookmarks ORDER BY createdAtEpoch DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: Long)

    @Query("DELETE FROM bookmarks WHERE globalIndex = :globalIndex")
    suspend fun deleteBookmarkByVerse(globalIndex: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE globalIndex = :globalIndex)")
    fun isVerseBookmarked(globalIndex: Int): Flow<Boolean>

    @Query("SELECT * FROM cached_verses WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getCachedVersesForSurah(surahNumber: Int): Flow<List<CachedVerseEntity>>

    @Query("SELECT * FROM cached_verses WHERE globalIndex = :globalIndex")
    suspend fun getCachedVerse(globalIndex: Int): CachedVerseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedVerses(verses: List<CachedVerseEntity>)
}

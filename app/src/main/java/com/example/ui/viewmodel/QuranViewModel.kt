package com.example.ui.viewmodel

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkEntity
import com.example.data.local.DailyLogEntity
import com.example.data.local.KhatamPlanEntity
import com.example.data.local.QuranDatabase
import com.example.data.local.UserProgressEntity
import com.example.data.model.PacingMode
import com.example.data.model.ScheduleSlot
import com.example.data.model.SurahInfo
import com.example.data.model.Verse
import com.example.data.quran.QuranRepository
import com.example.data.quran.SurahRegistry
import com.example.domain.PacingCalculator
import com.example.domain.PacingSummary
import com.example.notification.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class QuranNavTab(val title: String) {
    TODAY("Today's Pace"),
    PLAN("Khatam Plan"),
    SURAHS("Surahs"),
    READER("Recite"),
    PROGRESS("Progress")
}

data class QuranUiState(
    val plan: KhatamPlanEntity = KhatamPlanEntity(),
    val progress: UserProgressEntity = UserProgressEntity(),
    val todaySchedule: List<ScheduleSlot> = emptyList(),
    val pacingSummary: PacingSummary = PacingCalculator.computeSummary(
        KhatamPlanEntity(), UserProgressEntity(), 0, emptyList()
    ),
    val recentDailyLogs: List<DailyLogEntity> = emptyList(),
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val selectedSurah: SurahInfo = SurahRegistry.surahs.first(),
    val readerVerses: List<Verse> = emptyList(),
    val currentMomentVerse: Verse = com.example.data.quran.QuranVerseBank.getVerse(1),
    val todayCompletedVerses: Int = 0,
    val activeReadingGlobalIndex: Int = 1,
    val isPlayingAudio: Boolean = false,
    val currentlyPlayingVerse: Verse? = null,
    val arabicFontSizeSp: Float = 24f,
    val currentTab: QuranNavTab = QuranNavTab.TODAY,
    val snackbarMessage: String? = null
)

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuranRepository
    private var mediaPlayer: MediaPlayer? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    private val _selectedSurahNumber = MutableStateFlow(1)
    private val _activeReadingGlobalIndex = MutableStateFlow(1)
    private val _isPlayingAudio = MutableStateFlow(false)
    private val _currentlyPlayingVerse = MutableStateFlow<Verse?>(null)
    private val _arabicFontSizeSp = MutableStateFlow(24f)
    private val _currentTab = MutableStateFlow(QuranNavTab.TODAY)
    private val _snackbarMessage = MutableStateFlow<String?>(null)

    init {
        val database = QuranDatabase.getDatabase(application)
        repository = QuranRepository(database.quranDao())

        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }
    }

    val uiState: StateFlow<QuranUiState> = combine(
        repository.khatamPlan,
        repository.userProgress,
        repository.recentDailyLogs,
        repository.bookmarks,
        _selectedSurahNumber,
        _activeReadingGlobalIndex,
        _isPlayingAudio,
        _currentlyPlayingVerse,
        _arabicFontSizeSp,
        _currentTab,
        _snackbarMessage
    ) { args ->
        val plan = args[0] as KhatamPlanEntity
        val progress = args[1] as UserProgressEntity
        val logs = args[2] as List<DailyLogEntity>
        val bookmarks = args[3] as List<BookmarkEntity>
        val surahNum = args[4] as Int
        val activeGlobalIdx = args[5] as Int
        val isPlaying = args[6] as Boolean
        val playingVerse = args[7] as Verse?
        val fontSize = args[8] as Float
        val tab = args[9] as QuranNavTab
        val snackMsg = args[10] as String?

        val todayIso = dateFormat.format(Date())
        val todayLog = logs.find { it.dateIso == todayIso }
        val todayCompleted = todayLog?.versesReadCount ?: 0

        val schedule = PacingCalculator.generateDailySchedule(
            plan = plan,
            progress = progress,
            todayCompletedVerses = todayCompleted
        )

        val summary = PacingCalculator.computeSummary(
            plan = plan,
            progress = progress,
            todayCompletedVerses = todayCompleted,
            schedule = schedule
        )

        val selectedSurah = SurahRegistry.getSurah(surahNum)
        val readerVerses = repository.getVersesForSurah(surahNum)

        val currentMomentVerse = com.example.data.quran.QuranVerseBank.getVerse(progress.currentGlobalVerseIndex)

        QuranUiState(
            plan = plan,
            progress = progress,
            todaySchedule = schedule,
            pacingSummary = summary,
            recentDailyLogs = logs,
            bookmarks = bookmarks,
            selectedSurah = selectedSurah,
            readerVerses = readerVerses,
            currentMomentVerse = currentMomentVerse,
            todayCompletedVerses = todayCompleted,
            activeReadingGlobalIndex = activeGlobalIdx,
            isPlayingAudio = isPlaying,
            currentlyPlayingVerse = playingVerse,
            arabicFontSizeSp = fontSize,
            currentTab = tab,
            snackbarMessage = snackMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuranUiState()
    )

    fun switchTab(tab: QuranNavTab) {
        _currentTab.value = tab
    }

    fun openReaderAtVerse(globalIndex: Int) {
        val (surah, _) = SurahRegistry.getSurahAndAyahFromGlobalIndex(globalIndex)
        _selectedSurahNumber.value = surah.number
        _activeReadingGlobalIndex.value = globalIndex
        _currentTab.value = QuranNavTab.READER
    }

    fun selectSurah(surahNumber: Int) {
        val surah = SurahRegistry.getSurah(surahNumber)
        _selectedSurahNumber.value = surah.number
        _activeReadingGlobalIndex.value = surah.globalStartVerse
        _currentTab.value = QuranNavTab.READER
    }

    fun markVerseRead(globalIndex: Int) {
        viewModelScope.launch {
            repository.markVerseRead(globalIndex)
            _snackbarMessage.value = "Ayah marked as recited! Progress updated."
        }
    }

    fun markSlotRead(slot: ScheduleSlot) {
        viewModelScope.launch {
            repository.markRangeRead(slot.startGlobalVerse, slot.endGlobalVerse)
            _snackbarMessage.value = "Slot '${slot.label}' completed! (${slot.versesCount} verses read)."
        }
    }

    fun updatePlan(
        targetDays: Int,
        wakingStart: Int,
        wakingEnd: Int,
        mode: PacingMode,
        notifications: Boolean
    ) {
        viewModelScope.launch {
            repository.savePlan(
                targetDays = targetDays,
                wakingHourStart = wakingStart,
                wakingHourEnd = wakingEnd,
                pacingMode = mode,
                notificationsEnabled = notifications
            )

            // Reschedule notifications
            val updatedPlan = uiState.value.plan.copy(
                targetDays = targetDays,
                wakingHourStart = wakingStart,
                wakingHourEnd = wakingEnd,
                pacingMode = mode.name,
                notificationsEnabled = notifications
            )
            val updatedSchedule = PacingCalculator.generateDailySchedule(
                plan = updatedPlan,
                progress = uiState.value.progress,
                todayCompletedVerses = 0
            )

            ReminderScheduler.scheduleRemindersForSchedule(
                getApplication(),
                updatedPlan,
                updatedSchedule
            )

            _snackbarMessage.value = "Pacing plan updated successfully!"
        }
    }

    fun resetProgress(startGlobalIndex: Int = 1) {
        viewModelScope.launch {
            repository.resetProgress(startGlobalIndex)
            _activeReadingGlobalIndex.value = startGlobalIndex
            _snackbarMessage.value = "Khatam progress has been reset."
        }
    }

    fun toggleBookmark(globalIndex: Int, note: String = "") {
        viewModelScope.launch {
            val isBookmarked = uiState.value.bookmarks.any { it.globalIndex == globalIndex }
            if (isBookmarked) {
                repository.removeBookmark(globalIndex)
                _snackbarMessage.value = "Bookmark removed."
            } else {
                repository.toggleBookmark(globalIndex, note)
                _snackbarMessage.value = "Verse bookmarked!"
            }
        }
    }

    fun deleteBookmark(id: Long) {
        viewModelScope.launch {
            repository.deleteBookmarkById(id)
            _snackbarMessage.value = "Bookmark removed."
        }
    }

    fun setArabicFontSize(size: Float) {
        _arabicFontSizeSp.value = size.coerceIn(18f, 40f)
    }

    fun playAudioForVerse(verse: Verse) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(verse.audioUrl)
                setOnPreparedListener { mp ->
                    mp.start()
                    _isPlayingAudio.value = true
                    _currentlyPlayingVerse.value = verse
                }
                setOnCompletionListener {
                    _isPlayingAudio.value = false
                    _currentlyPlayingVerse.value = null
                }
                setOnErrorListener { _, _, _ ->
                    _isPlayingAudio.value = false
                    _currentlyPlayingVerse.value = null
                    _snackbarMessage.value = "Unable to stream recitation audio for this verse."
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _isPlayingAudio.value = false
            _currentlyPlayingVerse.value = null
            _snackbarMessage.value = "Audio playback error: ${e.message}"
        }
    }

    fun stopAudio() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            _isPlayingAudio.value = false
            _currentlyPlayingVerse.value = null
        } catch (_: Exception) {}
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun testReminderNotification() {
        ReminderScheduler.triggerTestNotification(getApplication())
        _snackbarMessage.value = "Reminder notification triggered! Check your notification bar."
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}

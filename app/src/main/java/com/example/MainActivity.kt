package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.PlanConfigScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.QuranReaderScreen
import com.example.ui.screens.SurahListScreen
import com.example.ui.screens.TodayPaceScreen
import com.example.ui.theme.BrightMint
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.QuranPacerTheme
import com.example.ui.viewmodel.QuranNavTab
import com.example.ui.viewmodel.QuranViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            QuranPacerTheme {
                val viewModel: QuranViewModel = viewModel()
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }

                // Show snackbars when available
                LaunchedEffect(state.snackbarMessage) {
                    state.snackbarMessage?.let { message ->
                        snackbarHostState.showSnackbar(message)
                        viewModel.clearSnackbar()
                    }
                }

                // Handle back button: if not on TODAY tab, go back to TODAY
                BackHandler(enabled = state.currentTab != QuranNavTab.TODAY) {
                    viewModel.switchTab(QuranNavTab.TODAY)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF0B0F19),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(NeonCyan)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (state.currentTab) {
                                            QuranNavTab.TODAY -> "KhatamLite"
                                            QuranNavTab.PLAN -> "Pacing Configuration"
                                            QuranNavTab.READER -> "Recitation • ${state.selectedSurah.nameEnglish}"
                                            QuranNavTab.SURAHS -> "All 114 Surahs"
                                            QuranNavTab.PROGRESS -> "Khatam Progress"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 19.sp,
                                        color = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF0F172A),
                                titleContentColor = Color.White
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("bottom_nav_bar"),
                            containerColor = Color(0xFF0F172A),
                            contentColor = Color.White
                        ) {
                            val items = listOf(
                                Triple(QuranNavTab.TODAY, Icons.Filled.Schedule, Icons.Outlined.Schedule),
                                Triple(QuranNavTab.PLAN, Icons.Filled.Tune, Icons.Outlined.Tune),
                                Triple(QuranNavTab.READER, Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
                                Triple(QuranNavTab.SURAHS, Icons.Filled.ListAlt, Icons.Outlined.ListAlt),
                                Triple(QuranNavTab.PROGRESS, Icons.Filled.Leaderboard, Icons.Outlined.Leaderboard)
                            )

                            items.forEach { (tab, filledIcon, outlinedIcon) ->
                                val selected = state.currentTab == tab
                                val labelText = when (tab) {
                                    QuranNavTab.TODAY -> "Moment"
                                    QuranNavTab.PLAN -> "Pace"
                                    QuranNavTab.READER -> "Recite"
                                    QuranNavTab.SURAHS -> "Surahs"
                                    QuranNavTab.PROGRESS -> "Progress"
                                }

                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { viewModel.switchTab(tab) },
                                    icon = {
                                        Icon(
                                            imageVector = if (selected) filledIcon else outlinedIcon,
                                            contentDescription = labelText
                                        )
                                    },
                                    label = { Text(labelText, fontSize = 11.sp) },
                                    modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}"),
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.Black,
                                        selectedTextColor = NeonCyan,
                                        unselectedIconColor = Color(0xFF64748B),
                                        unselectedTextColor = Color(0xFF64748B),
                                        indicatorColor = NeonCyan
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    val screenModifier = Modifier.padding(innerPadding)

                    when (state.currentTab) {
                        QuranNavTab.TODAY -> {
                            TodayPaceScreen(
                                state = state,
                                onOpenReaderAtVerse = { globalIndex ->
                                    viewModel.openReaderAtVerse(globalIndex)
                                },
                                onMarkSlotDone = { slot ->
                                    viewModel.markSlotRead(slot)
                                },
                                onNavigateToPlan = {
                                    viewModel.switchTab(QuranNavTab.PLAN)
                                },
                                onNavigateToSurahs = {
                                    viewModel.switchTab(QuranNavTab.SURAHS)
                                },
                                onMarkCurrentVerseRead = { globalIndex ->
                                    viewModel.markVerseRead(globalIndex)
                                },
                                onToggleBookmark = { globalIndex ->
                                    viewModel.toggleBookmark(globalIndex)
                                },
                                onPlayAudio = { verse ->
                                    viewModel.playAudioForVerse(verse)
                                },
                                onStopAudio = {
                                    viewModel.stopAudio()
                                },
                                modifier = screenModifier
                            )
                        }

                        QuranNavTab.PLAN -> {
                            PlanConfigScreen(
                                state = state,
                                onSavePlan = { days, wakeStart, wakeEnd, mode, notifications ->
                                    viewModel.updatePlan(days, wakeStart, wakeEnd, mode, notifications)
                                },
                                onTestNotification = {
                                    viewModel.testReminderNotification()
                                },
                                modifier = screenModifier
                            )
                        }

                        QuranNavTab.READER -> {
                            QuranReaderScreen(
                                state = state,
                                onSelectSurah = { surahNumber ->
                                    viewModel.selectSurah(surahNumber)
                                },
                                onMarkVerseRead = { globalIndex ->
                                    viewModel.markVerseRead(globalIndex)
                                },
                                onToggleBookmark = { globalIndex ->
                                    viewModel.toggleBookmark(globalIndex)
                                },
                                onPlayAudio = { verse ->
                                    viewModel.playAudioForVerse(verse)
                                },
                                onStopAudio = {
                                    viewModel.stopAudio()
                                },
                                onChangeFontSize = { newSize ->
                                    viewModel.setArabicFontSize(newSize)
                                },
                                modifier = screenModifier
                            )
                        }

                        QuranNavTab.SURAHS -> {
                            SurahListScreen(
                                currentSurahNumber = state.selectedSurah.number,
                                onSelectSurah = { surahNumber ->
                                    viewModel.selectSurah(surahNumber)
                                },
                                modifier = screenModifier
                            )
                        }

                        QuranNavTab.PROGRESS -> {
                            ProgressScreen(
                                state = state,
                                onOpenVerse = { globalIndex ->
                                    viewModel.openReaderAtVerse(globalIndex)
                                },
                                onDeleteBookmark = { bookmarkId ->
                                    viewModel.deleteBookmark(bookmarkId)
                                },
                                onResetProgress = { startVerse ->
                                    viewModel.resetProgress(startVerse)
                                },
                                modifier = screenModifier
                            )
                        }
                    }
                }
            }
        }
    }
}

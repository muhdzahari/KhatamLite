package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScheduleSlot
import com.example.data.model.Verse
import com.example.data.quran.SurahRegistry
import com.example.ui.theme.AuroraGradient
import com.example.ui.theme.BrightMint
import com.example.ui.theme.CosmicCardGradient
import com.example.ui.theme.ElectricIndigo
import com.example.ui.theme.ModernHeroGradient
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RadiantPurple
import com.example.ui.theme.SunsetGold
import com.example.ui.viewmodel.QuranUiState
import java.util.Calendar

@Composable
fun TodayPaceScreen(
    state: QuranUiState,
    onOpenReaderAtVerse: (Int) -> Unit,
    onMarkSlotDone: (ScheduleSlot) -> Unit,
    onNavigateToPlan: () -> Unit,
    onNavigateToSurahs: () -> Unit,
    onMarkCurrentVerseRead: (Int) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onPlayAudio: (Verse) -> Unit,
    onStopAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val verse = state.currentMomentVerse
    val surah = SurahRegistry.getSurah(verse.surahNumber)
    val summary = state.pacingSummary
    val isPlayingThis = state.isPlayingAudio && state.currentlyPlayingVerse?.globalIndex == verse.globalIndex
    val isBookmarked = state.bookmarks.any { it.globalIndex == verse.globalIndex }

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val isWakingTime = currentHour in state.plan.wakingHourStart until state.plan.wakingHourEnd

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("today_pace_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Sleek Modern Header Bar with Gradient Accents
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("modern_hero_header"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E1B4B),
                                    Color(0xFF0D2818)
                                )
                            )
                        )
                        .border(
                            1.dp,
                            Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.5f), BrightMint.copy(alpha = 0.3f))),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isWakingTime) BrightMint else SunsetGold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isWakingTime) "ACTIVE RECITATION WINDOW" else "SLEEP WINDOW EXCLUDED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    color = if (isWakingTime) BrightMint else SunsetGold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Verse for This Moment",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "${state.plan.targetDays}-Day Khatam • ${summary.versesPerSlotDisplay}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        // Streak Pill with Gradient
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0x33F59E0B),
                            border = BorderStroke(1.dp, SunsetGold.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = SunsetGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${state.progress.currentStreakDays}d Streak",
                                    color = SunsetGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. THE SINGLE VERSE OF THIS MOMENT (The core focus of the Home page)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("current_moment_verse_card")
                    .animateContentSize(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF1E293B),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(
                                    NeonCyan.copy(alpha = 0.8f),
                                    BrightMint.copy(alpha = 0.6f),
                                    RadiantPurple.copy(alpha = 0.4f)
                                )
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Verse Identity Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = surah.nameEnglish,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = surah.nameArabic,
                                        fontFamily = com.example.ui.theme.ArabicCalligraphyFontFamily,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                }
                                Text(
                                    text = "Ayah ${verse.ayahNumber} of ${surah.totalVerses} • Juz ${surah.juzNumber}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NeonCyan
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Audio playback button
                                IconButton(
                                    onClick = {
                                        if (isPlayingThis) onStopAudio() else onPlayAudio(verse)
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(if (isPlayingThis) BrightMint.copy(alpha = 0.2f) else Color(0x3338BDF8))
                                        .testTag("home_audio_button")
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingThis) Icons.Default.Pause else Icons.Default.VolumeUp,
                                        contentDescription = "Recite Audio",
                                        tint = if (isPlayingThis) BrightMint else NeonCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                // Bookmark button
                                IconButton(
                                    onClick = { onToggleBookmark(verse.globalIndex) },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x3338BDF8))
                                        .testTag("home_bookmark_button")
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isBookmarked) SunsetGold else Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Large Quran Uthmani Arabic Verse Rendering
                        Text(
                            text = verse.textArabic,
                            fontFamily = com.example.ui.theme.QuranUthmaniFontFamily,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 58.sp,
                            textAlign = TextAlign.End,
                            color = Color(0xFFF8FAFC),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_arabic_verse_text")
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Transliteration
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0x2200E5FF)
                        ) {
                            Text(
                                text = verse.transliteration,
                                style = MaterialTheme.typography.bodyMedium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = NeonCyan,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // English Translation
                        Text(
                            text = verse.translationEnglish,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // The Big Prominent "I've Recited This Verse" Action
                        Button(
                            onClick = { onMarkCurrentVerseRead(verse.globalIndex) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("mark_verse_recited_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(ModernHeroGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "I Have Recited This Verse",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Today's Progress Context Pill
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_summary_pill"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Today's Pacing Progress",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "${state.todayCompletedVerses} of ${summary.dailyTargetVerses} verses recited today",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "${(summary.percentCompletedToday * 100).toInt()}% Done",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrightMint
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { summary.percentCompletedToday },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = BrightMint,
                        trackColor = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Overall: ${state.progress.totalVersesCompleted} / 6,236 verses",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )

                        Text(
                            text = "Target Khatam: ${summary.projectedKhatamDateString.split(",").firstOrNull() ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = NeonCyan
                        )
                    }
                }
            }
        }

        // 4. Subtle Navigation Redirect to Full Quran Chapters
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chapters_navigation_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x221E293B)),
                border = BorderStroke(1.dp, Color(0x33334155))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Browse all 114 Quran chapters & full Surahs in the Surahs tab",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    TextButton(
                        onClick = onNavigateToSurahs,
                        modifier = Modifier.testTag("goto_surahs_tab_button")
                    ) {
                        Text("Open Surahs", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Verse
import com.example.data.quran.SurahRegistry
import com.example.ui.components.AyahNumberBadge
import com.example.ui.theme.AccentGoldDark
import com.example.ui.theme.AyatBorderGreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.viewmodel.QuranUiState
import kotlinx.coroutines.launch

@Composable
fun QuranReaderScreen(
    state: QuranUiState,
    onSelectSurah: (Int) -> Unit,
    onMarkVerseRead: (Int) -> Unit,
    onToggleBookmark: (Int) -> Unit,
    onPlayAudio: (Verse) -> Unit,
    onStopAudio: () -> Unit,
    onChangeFontSize: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val surah = state.selectedSurah
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll to the active reading verse when opened
    LaunchedEffect(state.activeReadingGlobalIndex, surah.number) {
        val targetIndex = state.readerVerses.indexOfFirst { it.globalIndex == state.activeReadingGlobalIndex }
        if (targetIndex >= 0) {
            // Add 1 to account for the header item
            listState.animateScrollToItem((targetIndex + 1).coerceAtMost(state.readerVerses.size))
        }
    }

    Box(modifier = modifier.fillMaxSize().testTag("quran_reader_screen")) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Surah Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("surah_header_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF0C4A6E))
                                )
                            )
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp))
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        // Previous / Next Surah selector row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (surah.number > 1) onSelectSurah(surah.number - 1)
                                },
                                enabled = surah.number > 1
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous Surah",
                                    tint = if (surah.number > 1) Color.White else Color.White.copy(alpha = 0.3f)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = surah.nameArabic,
                                    fontFamily = com.example.ui.theme.ArabicCalligraphyFontFamily,
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${surah.number}. ${surah.nameEnglish} (${surah.meaningEnglish})",
                                    color = AccentGoldDark,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (surah.number < 114) onSelectSurah(surah.number + 1)
                                },
                                enabled = surah.number < 114
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next Surah",
                                    tint = if (surah.number < 114) Color.White else Color.White.copy(alpha = 0.3f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${surah.revelationType} • ${surah.totalVerses} Ayahs",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Juz ${surah.juzNumber}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Font size control row
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(
                                onClick = { onChangeFontSize(state.arabicFontSizeSp - 2f) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("A-", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "Font: ${state.arabicFontSizeSp.toInt()}sp",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            IconButton(
                                onClick = { onChangeFontSize(state.arabicFontSizeSp + 2f) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("A+", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Bismillah (except for Surah At-Tawbah)
                        if (surah.number != 9 && surah.number != 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontFamily = com.example.ui.theme.QuranUthmaniFontFamily,
                                color = AccentGoldDark,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            }

            // 2. Audio Recitation Sticky Bar
            if (state.isPlayingAudio && state.currentlyPlayingVerse != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("audio_player_banner"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Reciting: Ayah ${state.currentlyPlayingVerse.ayahNumber}",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Text(
                                        text = "Sheikh Mishary Alafasy",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            IconButton(
                                onClick = onStopAudio,
                                modifier = Modifier.testTag("stop_audio_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = "Stop recitation",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // 3. Verses List
            items(state.readerVerses, key = { it.globalIndex }) { verse ->
                val isBookmarked = state.bookmarks.any { it.globalIndex == verse.globalIndex }
                val isRead = state.progress.currentGlobalVerseIndex > verse.globalIndex
                val isActiveReading = state.activeReadingGlobalIndex == verse.globalIndex
                val isPlayingThis = state.isPlayingAudio && state.currentlyPlayingVerse?.globalIndex == verse.globalIndex

                VerseCardItem(
                    verse = verse,
                    arabicFontSizeSp = state.arabicFontSizeSp,
                    isBookmarked = isBookmarked,
                    isRead = isRead,
                    isActiveReading = isActiveReading,
                    isPlaying = isPlayingThis,
                    onMarkRead = { onMarkVerseRead(verse.globalIndex) },
                    onToggleBookmark = { onToggleBookmark(verse.globalIndex) },
                    onPlayAudio = { onPlayAudio(verse) }
                )
            }
        }

        // Floating Bottom Reader Action Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Current: Ayah ${SurahRegistry.getSurahAndAyahFromGlobalIndex(state.progress.currentGlobalVerseIndex).second} of ${surah.nameEnglish}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${state.progress.totalVersesCompleted} / 6,236 Verses Khatam Read",
                        style = MaterialTheme.typography.labelSmall,
                        color = com.example.ui.theme.NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        val currentGlobal = state.progress.currentGlobalVerseIndex
                        onMarkVerseRead(currentGlobal)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    modifier = Modifier.testTag("mark_next_read_floating_button")
                ) {
                    Box(
                        modifier = Modifier
                            .background(com.example.ui.theme.ModernHeroGradient)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Recited & Next", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerseCardItem(
    verse: Verse,
    arabicFontSizeSp: Float,
    isBookmarked: Boolean,
    isRead: Boolean,
    isActiveReading: Boolean,
    isPlaying: Boolean,
    onMarkRead: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPlayAudio: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("verse_item_${verse.surahNumber}_${verse.ayahNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isPlaying -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                isActiveReading -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                isRead -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isActiveReading) {
            CardDefaults.outlinedCardBorder().copy(
                width = 1.5.dp,
                brush = androidx.compose.ui.graphics.SolidColor(EmeraldPrimary)
            )
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse Header: Ayah badge & Quick actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AyahNumberBadge(ayahNumber = verse.ayahNumber)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Audio Listen
                    IconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.VolumeUp,
                            contentDescription = "Listen Recitation",
                            tint = if (isPlaying) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Bookmark
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) AccentGoldDark else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Mark as Read
                    IconButton(
                        onClick = onMarkRead,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = "Mark as recited",
                            tint = if (isRead) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic text (Quran Uthmani standard, Right-to-Left alignment)
            Text(
                text = verse.textArabic,
                fontFamily = com.example.ui.theme.QuranUthmaniFontFamily,
                fontSize = (arabicFontSizeSp * 1.15f).sp,
                fontWeight = FontWeight.Normal,
                lineHeight = (arabicFontSizeSp * 2.1f).sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("arabic_text_${verse.ayahNumber}")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Transliteration
            Text(
                text = verse.transliteration,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // English Translation
            Text(
                text = verse.translationEnglish,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

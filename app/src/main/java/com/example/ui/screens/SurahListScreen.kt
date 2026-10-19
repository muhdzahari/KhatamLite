package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SurahInfo
import com.example.data.quran.SurahRegistry
import com.example.ui.theme.EmeraldPrimary

@Composable
fun SurahListScreen(
    currentSurahNumber: Int,
    onSelectSurah: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Meccan", "Medinan"

    val filteredSurahs = remember(searchQuery, selectedFilter) {
        SurahRegistry.surahs.filter { surah ->
            val matchesFilter = when (selectedFilter) {
                "Meccan" -> surah.revelationType.equals("Meccan", ignoreCase = true)
                "Medinan" -> surah.revelationType.equals("Medinan", ignoreCase = true)
                else -> true
            }

            val matchesQuery = searchQuery.isBlank() ||
                    surah.nameEnglish.contains(searchQuery, ignoreCase = true) ||
                    surah.nameArabic.contains(searchQuery, ignoreCase = true) ||
                    surah.meaningEnglish.contains(searchQuery, ignoreCase = true) ||
                    surah.number.toString() == searchQuery.trim()

            matchesFilter && matchesQuery
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("surah_list_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search TextField
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("surah_search_input"),
                placeholder = { Text("Search 114 Surahs by name or number...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
        }

        // Filter Chips (All, Meccan, Medinan)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Meccan", "Medinan").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = {
                            val count = when (filter) {
                                "Meccan" -> "86"
                                "Medinan" -> "28"
                                else -> "114"
                            }
                            Text("$filter ($count)")
                        },
                        modifier = Modifier.testTag("filter_${filter.lowercase()}_chip")
                    )
                }
            }
        }

        // Surah Items
        items(filteredSurahs, key = { it.number }) { surah ->
            val isCurrent = surah.number == currentSurahNumber

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectSurah(surah.number) }
                    .testTag("surah_item_${surah.number}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) Color(0xFF1E293B)
                    else Color(0xFF131B2A)
                ),
                border = if (isCurrent) {
                    androidx.compose.foundation.BorderStroke(1.5.dp, com.example.ui.theme.NeonCyan)
                } else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Surah Number Diamond/Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0x3300E5FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${surah.number}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = com.example.ui.theme.NeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = surah.nameEnglish,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${surah.meaningEnglish} • ${surah.totalVerses} Ayahs",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = surah.nameArabic,
                            fontFamily = com.example.ui.theme.ArabicCalligraphyFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = com.example.ui.theme.BrightMint
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1E293B)
                        ) {
                            Text(
                                text = "Juz ${surah.juzNumber}",
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

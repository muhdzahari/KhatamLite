package com.example.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PacingMode
import com.example.domain.PacingCalculator
import com.example.ui.theme.AuroraGradient
import com.example.ui.theme.BrightMint
import com.example.ui.theme.ModernHeroGradient
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.RadiantPurple
import com.example.ui.theme.SunsetGold
import com.example.ui.viewmodel.QuranUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanConfigScreen(
    state: QuranUiState,
    onSavePlan: (Int, Int, Int, PacingMode, Boolean) -> Unit,
    onTestNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDays by remember(state.plan.targetDays) { mutableIntStateOf(state.plan.targetDays) }

    var wakingHourStart by remember(state.plan.wakingHourStart) { mutableIntStateOf(state.plan.wakingHourStart) }
    var wakingHourEnd by remember(state.plan.wakingHourEnd) { mutableIntStateOf(state.plan.wakingHourEnd) }

    var selectedMode by remember(state.plan.pacingMode) {
        mutableStateOf(
            try {
                PacingMode.valueOf(state.plan.pacingMode)
            } catch (_: Exception) {
                PacingMode.HOURLY
            }
        )
    }

    var notificationsEnabled by remember(state.plan.notificationsEnabled) {
        mutableStateOf(state.plan.notificationsEnabled)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationsEnabled = true
        }
    }

    val activeHours = PacingCalculator.calculateWakingHours(wakingHourStart, wakingHourEnd)
    val sleepHours = 24 - activeHours
    val dailyTargetVerses = PacingCalculator.calculateDailyTarget(selectedDays)

    val yearsEquivalent = String.format("%.1f", selectedDays / 365f)

    val perSlotPreview = when (selectedMode) {
        PacingMode.HOURLY -> {
            val avg = dailyTargetVerses.toDouble() / activeHours
            if (avg < 1.0) "~1 verse every ${(1.0 / avg).roundToInt()} hrs"
            else String.format("%.1f verses / hour", avg)
        }
        PacingMode.EVERY_2_HOURS -> {
            val slots = (activeHours / 2).coerceAtLeast(1)
            String.format("%.1f verses every 2 hrs", dailyTargetVerses.toDouble() / slots)
        }
        PacingMode.SALAH_5_TIMES -> {
            String.format("%.1f verses per prayer", dailyTargetVerses.toDouble() / 5)
        }
        PacingMode.TWICE_DAILY -> {
            String.format("%.1f verses per session", dailyTargetVerses.toDouble() / 2)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("plan_config_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header
        item {
            Column {
                Text(
                    text = "Khatam Plan & Pacing",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Configure your Khatam pace (up to 5 years) and daily waking distribution.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // 2. Khatam Duration Target Card (Same design as Wake-up / Bedtime setting bar)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("khatam_duration_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
                border = BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "1. Target Khatam Duration",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Adjust the slider to choose your overall timeline from 1 month up to 5 years.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Target Duration Setting Bar (Identical style to Wake-up Time)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Target Timeline", fontWeight = FontWeight.Medium, color = Color.White, fontSize = 14.sp)
                        }
                        Text(
                            text = formatDurationLabel(selectedDays),
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }

                    Slider(
                        value = selectedDays.toFloat(),
                        onValueChange = { selectedDays = snapDuration(it.roundToInt()) },
                        valueRange = 30f..1825f,
                        steps = 59,
                        modifier = Modifier.testTag("khatam_duration_slider")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Milestone Preset Tags
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val quickPills = listOf(
                            30 to "1 Mo",
                            180 to "6 Mo",
                            365 to "1 Yr",
                            730 to "2 Yr",
                            1095 to "3 Yr",
                            1825 to "5 Yr"
                        )
                        quickPills.forEach { (days, label) ->
                            val isSelected = selectedDays == days
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) NeonCyan else Color(0xFF1E293B),
                                modifier = Modifier.clickable { selectedDays = days }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSelected) Color.Black else Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Visual Duration Info Bar (Identical design to the Sleep / Awake bar)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$dailyTargetVerses Verses / Day",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = BrightMint
                                )
                                Text(
                                    text = "$perSlotPreview • $activeHours waking hrs",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x3300E5FF)
                            ) {
                                Text(
                                    text = if (selectedDays >= 365) "$yearsEquivalent Years Goal" else "$selectedDays Days Goal",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Waking Hours & Sleeping Time Excluded
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("waking_hours_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
                border = BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "2. Daily Waking Hours (Excluding Sleep)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Verses are only scheduled while awake, completely excluding sleeping hours.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Wake Time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = SunsetGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Wake-up Time", fontWeight = FontWeight.Medium, color = Color.White, fontSize = 14.sp)
                        }
                        Text(
                            text = formatHourLabel(wakingHourStart),
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                    }

                    Slider(
                        value = wakingHourStart.toFloat(),
                        onValueChange = { wakingHourStart = it.roundToInt() },
                        valueRange = 4f..10f,
                        steps = 5,
                        modifier = Modifier.testTag("wake_time_slider")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bed Time
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bedtime, contentDescription = null, tint = BrightMint, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Bedtime (Sleep Start)", fontWeight = FontWeight.Medium, color = Color.White, fontSize = 14.sp)
                        }
                        Text(
                            text = formatHourLabel(wakingHourEnd),
                            fontWeight = FontWeight.Bold,
                            color = BrightMint
                        )
                    }

                    Slider(
                        value = wakingHourEnd.toFloat(),
                        onValueChange = { wakingHourEnd = it.roundToInt() },
                        valueRange = 20f..24f,
                        steps = 3,
                        modifier = Modifier.testTag("bed_time_slider")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sleep / Awake visual indicator
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E293B)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$activeHours Active Waking Hours",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = NeonCyan
                                )
                                Text(
                                    text = "$sleepHours Hours sleep time excluded",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0x3300E5FF)
                            ) {
                                Text(
                                    text = "${formatHourLabel(wakingHourStart)} – ${formatHourLabel(wakingHourEnd)}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Frequency Mode Selector Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("frequency_mode_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
                border = BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "3. Recitation Frequency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "How would you like today's verses divided throughout your active hours?",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PacingMode.values().forEach { mode ->
                        val isSelected = selectedMode == mode
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedMode = mode }
                                .testTag("mode_${mode.name.lowercase()}_card"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                            ),
                            border = if (isSelected) BorderStroke(1.5.dp, NeonCyan) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) NeonCyan else Color(0xFF334155)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mode.title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) NeonCyan else Color.White
                                    )
                                    Text(
                                        text = mode.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Live Calculation Preview Card with Modern Gradient
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_calculation_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2337)),
                border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(NeonCyan.copy(alpha = 0.4f), BrightMint.copy(alpha = 0.4f))))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = NeonCyan
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Automatic Pacing Formula",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Total Quran: 6,236 Verses ÷ $selectedDays Days ($yearsEquivalent yrs) = $dailyTargetVerses verses / day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Text(
                        text = "• Waking Window: $activeHours active hours (excluding $sleepHours hours sleep)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Text(
                        text = "• Pace at each moment: $perSlotPreview",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = BrightMint
                    )
                }
            }
        }

        // 6. Notifications & Reminders
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notification_settings_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2A)),
                border = BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (notificationsEnabled) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = null,
                                tint = NeonCyan
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Recitation Reminders",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "Gentle notifications at each moment",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    notificationsEnabled = enabled
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = Color(0xFF0C4A6E)),
                            modifier = Modifier.testTag("notification_toggle_switch")
                        )
                    }

                    AnimatedVisibility(visible = notificationsEnabled) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(
                                onClick = onTestNotification,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("test_notification_button")
                            ) {
                                Text("Send Test Reminder Notification Now", color = NeonCyan)
                            }
                        }
                    }
                }
            }
        }

        // 7. Save Plan Button (Modern Gradient)
        item {
            Button(
                onClick = {
                    onSavePlan(
                        selectedDays,
                        wakingHourStart,
                        wakingHourEnd,
                        selectedMode,
                        notificationsEnabled
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_plan_button"),
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
                    Text(
                        text = "Save KhatamLite Plan & Recalculate",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatHourLabel(hour: Int): String {
    val h = if (hour == 0 || hour == 24) 12 else if (hour > 12) hour - 12 else hour
    val ampm = if (hour < 12 || hour == 24) "AM" else "PM"
    return String.format("%02d:00 %s", h, ampm)
}

private fun formatDurationLabel(days: Int): String {
    return when {
        days <= 30 -> "30 Days (1 Month)"
        days == 180 -> "6 Months (180 Days)"
        days == 365 -> "1.0 Year (365 Days)"
        days == 730 -> "2.0 Years (730 Days)"
        days == 1095 -> "3.0 Years (1,095 Days)"
        days == 1825 -> "5.0 Years (1,825 Days)"
        days < 365 -> {
            val months = (days / 30f).roundToInt()
            "$days Days (~$months Mo)"
        }
        else -> {
            val years = String.format("%.1f", days / 365f)
            "$years Years ($days Days)"
        }
    }
}

private fun snapDuration(days: Int): Int {
    val presets = listOf(30, 180, 365, 730, 1095, 1460, 1825)
    for (preset in presets) {
        if (kotlin.math.abs(days - preset) <= 18) return preset
    }
    return days.coerceIn(30, 1825)
}

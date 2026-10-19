package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Modern Gradient App Palette
val DarkBg = Color(0xFF0B0F19)
val DarkSurface = Color(0xFF131B2A)
val DarkSurfaceCard = Color(0xFF1A2337)
val DarkSurfaceElevated = Color(0xFF222F49)
val DarkBorder = Color(0xFF2D3C58)

val LightBg = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceCard = Color(0xFFF1F5F9)
val LightSurfaceElevated = Color(0xFFE2E8F0)
val LightBorder = Color(0xFFCBD5E1)

// Electric & Radiant Accents
val NeonCyan = Color(0xFF00E5FF)
val BrightMint = Color(0xFF10B981)
val ElectricIndigo = Color(0xFF6366F1)
val RadiantPurple = Color(0xFF8B5CF6)
val SunsetGold = Color(0xFFF59E0B)
val SunsetAmber = Color(0xFFD97706)

// Theme compatibility aliases
val EmeraldPrimary = NeonCyan
val EmeraldPrimaryDark = BrightMint
val AccentGoldLight = SunsetGold
val AccentGoldDark = SunsetGold
val AyatBorderGreen = BrightMint

// Modern Gradients
val ModernHeroGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF00E5FF), Color(0xFF059669))
)

val AuroraGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF06B6D4), Color(0xFF10B981), Color(0xFF3B82F6))
)

val CosmicCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A243B), Color(0xFF111827))
)

val GoldAccentGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFBBF24), Color(0xFFF59E0B))
)

val GlassCardGradient = Brush.verticalGradient(
    colors = listOf(Color(0x3338BDF8), Color(0x0D0284C7))
)

val VerseGlowGradient = Brush.radialGradient(
    colors = listOf(Color(0x2600E5FF), Color(0x000B0F19))
)

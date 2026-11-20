package com.wouldyouratherextreme.game.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark, cool palette. Text colours are chosen to keep strong contrast on the
// backgrounds they sit on (we will double-check contrast again in later stages).
private val ExtremeColors = darkColorScheme(
    primary = Color(0xFFFF4D6D),
    onPrimary = Color(0xFF0B0D17),
    secondary = Color(0xFF4DA3FF),
    onSecondary = Color(0xFF0B0D17),
    background = Color(0xFF0B0D17),
    onBackground = Color(0xFFF4F5FA),
    surface = Color(0xFF151827),
    onSurface = Color(0xFFF4F5FA),
)

@Composable
fun WouldYouRatherExtremeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ExtremeColors, content = content)
}

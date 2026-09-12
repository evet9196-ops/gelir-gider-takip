package com.defter.harcama.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BgColor = Color(0xFF14261F)
val SurfaceColor = Color(0xFF1C332A)
val SurfaceColor2 = Color(0xFF22392F)
val LineColor = Color(0xFF2E4A3C)
val TextColor = Color(0xFFF3EFE6)
val TextDim = Color(0xFF9FB6A8)
val GoldColor = Color(0xFFD9B44A)
val RustColor = Color(0xFFC1656A)

private val DefterColors = darkColorScheme(
    background = BgColor,
    surface = SurfaceColor,
    primary = GoldColor,
    secondary = RustColor,
    onBackground = TextColor,
    onSurface = TextColor,
    onPrimary = Color(0xFF1B2E22)
)

@Composable
fun DefterTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DefterColors, content = content)
}

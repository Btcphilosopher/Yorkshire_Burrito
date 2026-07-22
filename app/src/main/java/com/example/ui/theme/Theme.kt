package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldLight,
    onPrimary = GravyBronze,
    primaryContainer = YorkshireGreen,
    onPrimaryContainer = CreamWhite,
    secondary = GoldLight,
    onSecondary = GravyBronze,
    tertiary = YorkshireGreen,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = OnDarkSurface,
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    surfaceVariant = Color(0xFF38231C),
    onSurfaceVariant = GoldLight
  )

private val LightColorScheme =
  lightColorScheme(
    primary = YorkshireGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF5E7), // Super soft warm gold/cream
    onPrimaryContainer = GravyBronze,
    secondary = RoastBrown,
    onSecondary = Color.White,
    tertiary = YorkshireGold,
    onTertiary = GravyBronze,
    background = CreamWhite,
    onBackground = GravyBronze,
    surface = Color.White,
    onSurface = GravyBronze,
    surfaceVariant = Color(0xFFF5EFEB),
    onSurfaceVariant = RoastBrown
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color disabled to ensure consistent brand colors
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

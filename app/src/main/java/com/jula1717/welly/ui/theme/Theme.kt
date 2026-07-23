package com.jula1717.welly.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SageGreen40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8DF),
    onPrimaryContainer = Color(0xFF0F2A20),
    secondary = Sand40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF0E6D6),
    onSecondaryContainer = Color(0xFF2A2114),
    tertiary = SoftCoral40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF8D9D6),
    onTertiaryContainer = Color(0xFF3A1412),
    background = WarmBackgroundLight,
    onBackground = Color(0xFF1B1C1A),
    surface = WarmSurfaceLight,
    onSurface = Color(0xFF1B1C1A),
    surfaceVariant = Color(0xFFE4E7E2),
    onSurfaceVariant = Color(0xFF424843),
)

private val DarkColorScheme = darkColorScheme(
    primary = SageGreen80,
    onPrimary = Color(0xFF0F2A20),
    primaryContainer = Color(0xFF274F41),
    onPrimaryContainer = Color(0xFFD5E8DF),
    secondary = Sand80,
    onSecondary = Color(0xFF2A2114),
    secondaryContainer = Color(0xFF52432F),
    onSecondaryContainer = Color(0xFFF0E6D6),
    tertiary = SoftCoral80,
    onTertiary = Color(0xFF3A1412),
    tertiaryContainer = Color(0xFF7A3A36),
    onTertiaryContainer = Color(0xFFF8D9D6),
    background = WarmBackgroundDark,
    onBackground = Color(0xFFE3E3DE),
    surface = WarmSurfaceDark,
    onSurface = Color(0xFFE3E3DE),
    surfaceVariant = Color(0xFF424843),
    onSurfaceVariant = Color(0xFFC2C8C1),
)

@Composable
fun WellyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

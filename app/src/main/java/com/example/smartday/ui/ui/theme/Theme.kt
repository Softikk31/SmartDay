package com.example.smartday.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Yellow,
    surface = BlackDeep,
    onSurface = Color.White,
    onSurfaceVariant = GrayLight,
    surfaceContainer = GrayDark,
    surfaceContainerHigh = BlackSoft,
    surfaceContainerHighest = BlackDark,
    error = Red
)

private val LightColorScheme = lightColorScheme(
    primary = Yellow,
    surface = White,
    onSurface = Color.Black,
    onSurfaceVariant = GrayLight,
    surfaceContainer = WhiteStone,
    surfaceContainerHigh = WhiteSoft,
    surfaceContainerHighest = WhiteWarm,
    error = Red
)


@Composable
fun SmartDayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
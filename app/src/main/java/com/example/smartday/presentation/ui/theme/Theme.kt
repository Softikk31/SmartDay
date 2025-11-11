package com.example.smartday.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = Amber,
    surface = DeepBlack,
    onSurface = Color.White,
    onSurfaceVariant = AshGray,
    surfaceContainer = IronGray,
    surfaceContainerHigh = Graphite,
    surfaceContainerHighest = Charcoal,
    error = Crimson
)

private val LightColorScheme = lightColorScheme(
    primary = Amber,
    surface = PureWhite,
    onSurface = Color.Black,
    onSurfaceVariant = AshGray,
    surfaceContainer = StoneWhite,
    surfaceContainerHigh = MistWhite,
    surfaceContainerHighest = IvoryWhite,
    error = Crimson
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
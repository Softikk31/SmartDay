package com.example.smartday.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.ui.main.view_models.ThemeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel

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
    content: @Composable () -> Unit
) {
    val themeViewModel: ThemeViewModel = koinViewModel()
    val theme by themeViewModel.theme.collectAsState()

    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        darkIcons = when {
            theme.systemTheme -> !isSystemInDarkTheme()
            theme.isDarkMode -> false
            else -> true
        },
        color = Color.Transparent
    )


    val darkTheme = if (theme.systemTheme) isSystemInDarkTheme() else theme.isDarkMode

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val primary = when (theme.primaryColor) {
        ThemePrimaryColors.YELLOW -> Yellow
        ThemePrimaryColors.RED -> Red
        ThemePrimaryColors.BLUE -> Blue
        ThemePrimaryColors.ORANGE -> Orange
        ThemePrimaryColors.GREEN -> Green
        ThemePrimaryColors.PURPLE -> Purple
    }

    MaterialTheme(
        colorScheme = colorScheme.copy(primary = primary),
        typography = Typography,
        content = content
    )
}
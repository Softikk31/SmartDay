package com.example.smartday.ui.models.states

import com.example.smartday.core.enums.ThemePrimaryColors

data class ThemeState(
    val id: Int = 1,
    val primaryColor: ThemePrimaryColors = ThemePrimaryColors.YELLOW,
    val systemTheme: Boolean = true,
    val isDarkMode: Boolean = true
)

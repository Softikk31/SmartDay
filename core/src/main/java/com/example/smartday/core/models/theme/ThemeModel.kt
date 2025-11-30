package com.example.smartday.core.models.theme

import com.example.smartday.core.enums.ThemePrimaryColors

data class ThemeModel(
    val id: Int = 1,
    val primaryColor: ThemePrimaryColors = ThemePrimaryColors.YELLOW,
    val systemTheme: Boolean = true,
    val isDarkMode: Boolean = false
)
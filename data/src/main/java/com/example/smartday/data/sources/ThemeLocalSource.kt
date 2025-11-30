package com.example.smartday.data.sources

import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.core.models.theme.ThemeModel
import kotlinx.coroutines.flow.Flow

interface ThemeLocalSource {
    suspend fun updateSystemTheme(isSystem: Boolean)
    suspend fun updateThemeMode(isDark: Boolean)
    suspend fun updatePrimaryTheme(color: ThemePrimaryColors)
    fun getTheme(): Flow<ThemeModel>
}
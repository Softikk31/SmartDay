package com.example.smartday.data.repository

import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.core.models.theme.ThemeModel
import com.example.smartday.data.sources.ThemeLocalSource
import com.example.smartday.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeRepositoryImpl(
    private val localSource: ThemeLocalSource
) : ThemeRepository {
    override suspend fun updateSystemTheme(isSystem: Boolean) {
        localSource.updateSystemTheme(isSystem = isSystem)
    }

    override suspend fun updateThemeMode(isDark: Boolean) {
        localSource.updateThemeMode(isDark = isDark)
    }

    override suspend fun updatePrimaryTheme(color: ThemePrimaryColors) {
        localSource.updatePrimaryTheme(color = color)
    }

    override fun getTheme(): Flow<ThemeModel> {
        return localSource.getTheme()
    }
}
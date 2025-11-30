package com.example.smartday.data.sources.local

import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.core.models.theme.ThemeModel
import com.example.smartday.data.mappers.toEntity
import com.example.smartday.data.mappers.toModel
import com.example.smartday.data.sources.ThemeLocalSource
import com.example.smartday.data.sources.local.room.ThemeDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ThemeLocalSourceImpl(
    private val taskDao: ThemeDao
) : ThemeLocalSource {
    override suspend fun updateSystemTheme(isSystem: Boolean) {
        taskDao.updateSystemTheme(isSystem = isSystem)
    }

    override suspend fun updateThemeMode(isDark: Boolean) {
        taskDao.updateThemeMode(isDark = isDark)
    }

    override suspend fun updatePrimaryTheme(color: ThemePrimaryColors) {
        taskDao.updatePrimaryTheme(color = color)
    }

    override fun getTheme(): Flow<ThemeModel> {
        return taskDao.getTheme().map { theme ->
            if (theme?.toModel() == null) {
                taskDao.insertTheme(
                    theme = ThemeModel().toEntity()
                )
                ThemeModel()
            } else {
                theme.toModel()
            }
        }
    }
}

package com.example.smartday.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.data.sources.local.entities.ThemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDao {
    @Query("UPDATE theme SET system_theme = :isSystem WHERE id == 1")
    suspend fun updateSystemTheme(isSystem: Boolean)

    @Query("UPDATE theme SET theme_model = :isDark WHERE id == 1")
    suspend fun updateThemeMode(isDark: Boolean)

    @Query("UPDATE theme SET primary_color = :color WHERE id == 1")
    suspend fun updatePrimaryTheme(color: ThemePrimaryColors)

    @Query("SELECT * FROM theme")
    fun getTheme(): Flow<ThemeEntity?>

    @Insert
    suspend fun insertTheme(theme: ThemeEntity)
}
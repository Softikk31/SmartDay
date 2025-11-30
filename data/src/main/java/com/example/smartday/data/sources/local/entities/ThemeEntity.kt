package com.example.smartday.data.sources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartday.core.enums.ThemePrimaryColors

@Entity(tableName = "theme")
data class ThemeEntity(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int = 1,
    @ColumnInfo("primary_color", defaultValue = "YELLOW")
    val primaryColor: ThemePrimaryColors,
    @ColumnInfo("system_theme", defaultValue = "true")
    val systemTheme: Boolean,
    @ColumnInfo("theme_model", defaultValue = "true")
    val isDarkMode: Boolean
)

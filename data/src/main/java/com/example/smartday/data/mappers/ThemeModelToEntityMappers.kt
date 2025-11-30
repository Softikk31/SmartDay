package com.example.smartday.data.mappers

import com.example.smartday.core.models.theme.ThemeModel
import com.example.smartday.data.sources.local.entities.ThemeEntity

fun ThemeModel.toEntity() = ThemeEntity(
    id = id,
    primaryColor = primaryColor,
    systemTheme = systemTheme,
    isDarkMode = isDarkMode,
)

fun ThemeEntity.toModel() = ThemeModel(
    id = id,
    primaryColor = primaryColor,
    systemTheme = systemTheme,
    isDarkMode = isDarkMode,
)
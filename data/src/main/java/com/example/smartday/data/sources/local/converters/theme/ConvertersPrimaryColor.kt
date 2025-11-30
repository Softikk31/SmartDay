package com.example.smartday.data.sources.local.converters.theme

import androidx.room.TypeConverter
import com.example.smartday.core.enums.ThemePrimaryColors

class ConvertersPrimaryColor {
    @TypeConverter
    fun fromThemePrimaryColors(themeColor: ThemePrimaryColors?): String? {
        return themeColor?.name
    }

    @TypeConverter
    fun toThemePrimaryColors(themeColor: String?): ThemePrimaryColors? {
        return if (themeColor == null) {
            null
        } else {
            ThemePrimaryColors.valueOf(themeColor)
        }
    }
}
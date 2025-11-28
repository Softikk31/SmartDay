package com.example.smartday.ui.utils

import android.content.Context
import java.time.LocalDate
import java.util.Locale
import com.example.smartday.R
import java.time.format.DateTimeFormatter

fun LocalDate.toDisplayString(locale: Locale, context: Context): String {
    val today = LocalDate.now()
    return when (this) {
        today -> {
            context.getString(R.string.today)
        }

        today.plusDays(1) -> {
            context.getString(R.string.tomorrow)
        }

        today.plusDays(2) -> {
            context.getString(R.string.after_tomorrow)
        }

        else -> this.format(
            DateTimeFormatter.ofPattern("d.MM.yy", locale)
        )
    }
}

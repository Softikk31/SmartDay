package com.example.smartday.ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@Composable
fun DaysOfWeekTitle(modifier: Modifier = Modifier, daysOfWeek: List<DayOfWeek>, locale: Locale?) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, locale).lowercase(),
                color = if (daysOfWeek.indexOf(dayOfWeek) in arrayOf(5, 6))
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
package com.example.smartday.ui.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.*
import java.time.format.TextStyle as DateTextStyle

@Composable
fun SelectedDateText(
    modifier: Modifier = Modifier,
    date: LocalDate?,
    locale: Locale?,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    year: Boolean = false,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues)
    ) {
        val dayName = date
            ?.dayOfWeek
            ?.getDisplayName(DateTextStyle.SHORT_STANDALONE, locale)
            ?.replaceFirstChar { it.uppercase() }

        Text(
            text = if (year) "$dayName, ${date?.dayOfMonth} ${
                date?.month?.getDisplayName(DateTextStyle.FULL_STANDALONE, locale)
            }, ${date?.year}" else "$dayName, ${date?.dayOfMonth} ${
                date?.month?.getDisplayName(DateTextStyle.FULL_STANDALONE, locale)
            }",
            style = style,
            color = color
        )
    }
}
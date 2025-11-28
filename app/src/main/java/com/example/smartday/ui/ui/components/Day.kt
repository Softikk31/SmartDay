package com.example.smartday.ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.WeekDay
import java.time.LocalDate

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit,
    enabledPastDays: Boolean = true
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .background(
                color = if ((day.position == DayPosition.MonthDate) and isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (day.position == DayPosition.MonthDate) {
                    if (enabledPastDays) {
                        onClick(day)
                    } else if (LocalDate.now() <= day.date) {
                        onClick(day)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (day.position == DayPosition.MonthDate) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabledPastDays) {
                    if ((day.date == LocalDate.now()) and !isSelected) MaterialTheme.colorScheme.primary
                    else if (isSelected) Color.White
                    else MaterialTheme.colorScheme.onSurface
                } else {
                    if (LocalDate.now() > day.date) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        if ((day.date == LocalDate.now()) and !isSelected) MaterialTheme.colorScheme.primary
                        else if (isSelected) Color.White
                        else MaterialTheme.colorScheme.onSurface
                    }
                }
            )
        }
    }
}

@Composable
fun Day(
    day: WeekDay,
    isSelected: Boolean,
    onClick: (WeekDay) -> Unit,
    enabledPastDays: Boolean = true
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(6.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (enabledPastDays) {
                    onClick(day)
                } else if (LocalDate.now() <= day.date) {
                    onClick(day)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabledPastDays) {
                if ((day.date == LocalDate.now()) and !isSelected) MaterialTheme.colorScheme.primary
                else if (isSelected) Color.White
                else MaterialTheme.colorScheme.onSurface
            } else {
                if (LocalDate.now() > day.date) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    if ((day.date == LocalDate.now()) and !isSelected) MaterialTheme.colorScheme.primary
                    else if (isSelected) Color.White
                    else MaterialTheme.colorScheme.onSurface
                }
            }
        )
    }
}

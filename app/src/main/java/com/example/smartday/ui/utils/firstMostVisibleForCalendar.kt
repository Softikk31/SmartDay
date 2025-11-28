package com.example.smartday.ui.utils

import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarLayoutInfo
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.Week

fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

fun WeekCalendarLayoutInfo.firstMostVisibleWeek(viewportPercent: Float = 50f): Week? {
    if (visibleWeeksInfo.isEmpty()) return null

    val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f

    return visibleWeeksInfo.firstOrNull { itemInfo ->
        if (itemInfo.offset < 0) {
            itemInfo.offset + itemInfo.size >= viewportSize
        } else {
            itemInfo.size - itemInfo.offset >= viewportSize
        }
    }?.week
}
package com.example.smartday.core.models.task.repetition.values

import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class DayOfWeekRepetition(
    val value: DayOfWeek
) : RepetitionValue

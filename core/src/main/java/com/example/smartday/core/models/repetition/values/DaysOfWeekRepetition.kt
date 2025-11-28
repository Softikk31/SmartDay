package com.example.smartday.core.models.repetition.values

import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class DaysOfWeekRepetition(
    val value: List<DayOfWeek>
) : RepetitionValue

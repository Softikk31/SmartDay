package com.example.smartday.core.models.task.repetition.values

import kotlinx.serialization.Serializable

@Serializable
data class MonthDayRepetition(
    val value: Int
) : RepetitionValue

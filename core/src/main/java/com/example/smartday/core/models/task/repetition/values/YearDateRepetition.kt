package com.example.smartday.core.models.task.repetition.values

import kotlinx.serialization.Serializable

@Serializable
data class YearDateRepetition(
    val value: ValueDateRepetition
) : RepetitionValue

package com.example.smartday.core.models.repetition.values

import kotlinx.serialization.Serializable

@Serializable
data class ValueDateRepetition(
    val month: Int,
    val dayOfMonth: Int
)

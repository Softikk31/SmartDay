package com.example.smartday.core.models.task

import kotlinx.serialization.Serializable

@Serializable
data class SubtaskModel(
    val id: Long = 0,
    val title: String = "",
    val isCompleted: Boolean = false
)

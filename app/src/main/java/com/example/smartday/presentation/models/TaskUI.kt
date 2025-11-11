package com.example.smartday.presentation.models

import java.time.LocalDate
import java.time.LocalTime

data class TaskUI(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isOverdue: Boolean = false,
    val date: LocalDate? = null,
    val notification: LocalTime? = null
)

package com.example.smartday.domain.models

import java.time.LocalDate
import java.time.LocalTime

data class TaskDomain(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isOverdue: Boolean = false,
    val date: LocalDate? = null,
    val notification: LocalTime? = null
)
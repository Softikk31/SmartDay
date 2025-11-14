package com.example.smartday.presentation.models

import com.example.smartday.presentation.mappers.TaskPriorityUI
import java.time.LocalDate
import java.time.LocalTime

data class TaskUI(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isOverdue: Boolean = false,
    val priority: TaskPriorityUI = TaskPriorityUI.NULL,
    val date: LocalDate? = null,
    val notification: LocalTime? = null
)

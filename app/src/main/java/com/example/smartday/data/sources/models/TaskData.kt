package com.example.smartday.data.sources.models

import com.example.smartday.data.sources.local.converters.TaskPriorityData
import java.time.LocalDate
import java.time.LocalTime

data class TaskData(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isOverdue: Boolean = false,
    val priority: TaskPriorityData = TaskPriorityData.NULL,
    val date: LocalDate? = null,
    val notification: LocalTime? = null
)

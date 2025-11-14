package com.example.smartday.presentation.models.states

import com.example.smartday.presentation.mappers.TaskPriorityUI
import java.time.LocalDate
import java.time.LocalTime

data class TaskState(
    val id: Long? = null,
    val title: String = "",
    val priority: TaskPriorityUI = TaskPriorityUI.NULL,
    val date: LocalDate? = null,
    val time: LocalTime? = null
)
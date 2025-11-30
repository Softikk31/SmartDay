package com.example.smartday.ui.models.states

import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import java.time.LocalDate
import java.time.LocalTime

data class TaskFormState(
    val id: Long? = null,
    val title: String = "",
    val priority: TaskPriority = TaskPriority.NULL,
    val repetition: TaskRepetitionModel = TaskRepetitionModel(),
    val date: LocalDate? = null,
    val time: LocalTime? = null
)
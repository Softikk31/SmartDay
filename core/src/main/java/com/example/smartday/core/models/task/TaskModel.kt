package com.example.smartday.core.models.task

import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class TaskModel(
    val id: Long = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val isOverdue: Boolean = false,
    val repetition: TaskRepetitionModel =
        TaskRepetitionModel(
            null,
            TaskTypeRepetition.OnSystemTypeRepetition(TaskTypeRepetitionSystem.NULL)
        ),
    val priority: TaskPriority = TaskPriority.NULL,
    val date: LocalDate? = null,
    val notification: LocalTime? = null
)

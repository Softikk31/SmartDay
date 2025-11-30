package com.example.smartday.core.models.task.repetition

import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.repetition.values.RepetitionValue
import kotlinx.serialization.Serializable

@Serializable
data class TaskRepetitionModel(
    val value: RepetitionValue? = null,
    val type: TaskTypeRepetition = TaskTypeRepetition.OnSystemTypeRepetition(
        TaskTypeRepetitionSystem.NULL
    ),
    val counter: Int = 1
)
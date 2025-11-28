package com.example.smartday.ui.models.states

import com.example.smartday.core.models.TaskModel

data class TaskFoundState(
    val matchesTitle: Map<String, List<TaskModel>> = emptyMap()
)

package com.example.smartday.presentation.mappers

import com.example.smartday.data.sources.local.converters.TaskPriorityData
import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.presentation.models.TaskUI

fun TaskDomain.toUI() = TaskUI(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    priority = toTaskPriorityUI(priority),
    date = date,
    notification = notification
)

fun toTaskPriorityUI(priority: TaskPriorityData): TaskPriorityUI = TaskPriorityUI.valueOf(priority.name)

enum class TaskPriorityUI {
    NULL,
    LOW,
    MEDIUM,
    HIGH
}
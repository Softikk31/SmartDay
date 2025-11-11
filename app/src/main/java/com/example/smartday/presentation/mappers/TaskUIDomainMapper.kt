package com.example.smartday.presentation.mappers

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.presentation.models.TaskUI

fun TaskDomain.toUI() = TaskUI(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    date = date,
    notification = notification
)


fun TaskUI.toDomain() = TaskDomain(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    date = date,
    notification = notification
)

package com.example.smartday.data.mappers

import com.example.smartday.core.models.TaskModel
import com.example.smartday.data.sources.local.entities.TaskEntity

fun TaskModel.toEntity() = TaskEntity(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    repetition = repetition,
    priority = priority,
    date = date,
    notification = notification
)


fun TaskEntity.toModel() = TaskModel(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    repetition = repetition,
    priority = priority,
    date = date,
    notification = notification
)

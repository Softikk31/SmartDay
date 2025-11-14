package com.example.smartday.data.mappers

import com.example.smartday.data.sources.models.TaskData
import com.example.smartday.domain.models.TaskDomain

fun TaskDomain.toData() = TaskData(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    priority = priority,
    date = date,
    notification = notification
)

fun TaskData.toDomain() = TaskDomain(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    priority = priority,
    date = date,
    notification = notification
)
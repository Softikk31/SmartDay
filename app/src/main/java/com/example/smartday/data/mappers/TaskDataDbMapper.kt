package com.example.smartday.data.mappers

import com.example.smartday.data.sources.models.TaskData
import com.example.smartday.data.sources.local.entities.TaskDB

fun TaskData.toDB() = TaskDB(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    priority = priority,
    date = date,
    notification = notification
)


fun TaskDB.toData() = TaskData(
    id = id,
    title = title,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
    priority = priority,
    date = date,
    notification = notification
)

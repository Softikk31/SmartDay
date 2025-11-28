package com.example.smartday.data.sources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.repetition.TaskRepetitionModel
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "is_completed", defaultValue = "false")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "is_overdue", defaultValue = "false")
    val isOverdue: Boolean = false,
    @ColumnInfo(name = "repetition")
    val repetition: TaskRepetitionModel = TaskRepetitionModel(
        null,
        TaskTypeRepetition.OnSystemTypeRepetition(TaskTypeRepetitionSystem.NULL)
    ),
    @ColumnInfo(name = "priority", defaultValue = "0")
    val priority: TaskPriority = TaskPriority.NULL,
    @ColumnInfo(name = "date", defaultValue = "null")
    val date: LocalDate? = null,
    @ColumnInfo(name = "notification", defaultValue = "null")
    val notification: LocalTime? = null
)
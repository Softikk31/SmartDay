package com.example.smartday.data.sources.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smartday.data.sources.local.converters.TaskPriorityData
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "tasks")
data class TaskDB(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "is_completed", defaultValue = "false")
    val isCompleted: Boolean = false,
    @ColumnInfo(name = "is_overdue", defaultValue = "false")
    val isOverdue: Boolean = false,
    @ColumnInfo(name = "priority", defaultValue = "0")
    val priority: TaskPriorityData = TaskPriorityData.NULL,
    @ColumnInfo(name = "date", defaultValue = "null")
    val date: LocalDate? = null,
    @ColumnInfo(name = "notification", defaultValue = "null")
    val notification: LocalTime? = null
)
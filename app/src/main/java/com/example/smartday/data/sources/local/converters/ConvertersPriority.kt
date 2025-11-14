package com.example.smartday.data.sources.local.converters

import androidx.room.TypeConverter

class ConvertersPriority {
    @TypeConverter
    fun fromTaskPriority(priority: TaskPriorityData): Int {
        return priority.ordinal
    }

    @TypeConverter
    fun toTaskPriority(ordinalPriority: Int): TaskPriorityData {
        for (priority in TaskPriorityData.entries) {
            if (priority.ordinal == ordinalPriority) {
                return priority
            }
        }
        return TaskPriorityData.NULL
    }
}

enum class TaskPriorityData {
    NULL,
    LOW,
    MEDIUM,
    HIGH
}
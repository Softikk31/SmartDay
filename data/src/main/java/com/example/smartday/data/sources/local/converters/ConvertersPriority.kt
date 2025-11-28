package com.example.smartday.data.sources.local.converters

import androidx.room.TypeConverter
import com.example.smartday.core.enums.TaskPriority

class ConvertersPriority {
    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): Int {
        return priority.ordinal
    }

    @TypeConverter
    fun toTaskPriority(ordinalPriority: Int): TaskPriority {
        for (priority in TaskPriority.entries) {
            if (priority.ordinal == ordinalPriority) {
                return priority
            }
        }
        return TaskPriority.NULL
    }
}
package com.example.smartday.data.sources.local.converters.task

import androidx.room.TypeConverter
import com.example.smartday.core.models.task.SubtaskModel
import kotlinx.serialization.json.Json

class ConvertersSubtask {
    @TypeConverter
    fun fromSubtask(subtasks: List<SubtaskModel>): String {
        return Json.encodeToString(subtasks)
    }

    @TypeConverter
    fun toSubtask(data: String): List<SubtaskModel> {
        return Json.decodeFromString(data)
    }
}
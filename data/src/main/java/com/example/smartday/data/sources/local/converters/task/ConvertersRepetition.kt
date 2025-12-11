package com.example.smartday.data.sources.local.converters.task

import androidx.room.TypeConverter
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import kotlinx.serialization.json.Json

class ConvertersRepetition {
    @TypeConverter
    fun fromTaskRepetition(repetition: TaskRepetitionModel): String {
        return Json.encodeToString(repetition)
    }

    @TypeConverter
    fun toTaskRepetition(data: String): TaskRepetitionModel {
        return Json.decodeFromString(data)
    }
}

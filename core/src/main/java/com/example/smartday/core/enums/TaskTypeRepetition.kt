package com.example.smartday.core.enums

import kotlinx.serialization.Serializable

@Serializable
enum class TaskTypeRepetitionSystem {
    NULL,
    EVERY_DAY,
    EVERY_WEEK_ON,
    EVERY_WORKDAY,
    EVERY_MONTH_DATE,
    EVERY_YEAR_DATE
}

@Serializable
enum class TaskTypeRepetitionCustom {
    DAY,
    WEEK,
    MONTH,
    YEAR
}

@Serializable
sealed class TaskTypeRepetition {
    @Serializable
    data class OnSystemTypeRepetition(val enumClass: TaskTypeRepetitionSystem) : TaskTypeRepetition()
    @Serializable
    data class OnCustomTypeRepetition(val enumClass: TaskTypeRepetitionCustom) : TaskTypeRepetition()
}
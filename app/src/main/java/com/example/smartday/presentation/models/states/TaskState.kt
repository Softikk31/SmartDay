package com.example.smartday.presentation.models.states

import java.time.LocalDate
import java.time.LocalTime

data class TaskState(
    val id: Long? = null,
    val title: String = "",
    val date: LocalDate? = null,
    val time: LocalTime? = null
)
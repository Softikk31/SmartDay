package com.example.smartday.ui.models.states

data class TaskErrorState(
    val typeError: Throwable? = null,
    val message: String? = null
)
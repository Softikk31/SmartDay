package com.example.smartday.ui.models.states

data class SelectModeState(
    val isDeleting: Boolean = false,
    val selectAll: Boolean = false,
    val selectedIds: List<Long> = emptyList()
)

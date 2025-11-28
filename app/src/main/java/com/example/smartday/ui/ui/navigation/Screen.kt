package com.example.smartday.ui.ui.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Home : Screen()
    @Serializable
    object Tasks : Screen()
    @Serializable
    object Task : Screen()
    @Serializable
    object Search : Screen()
    @Serializable
    object Settings : Screen()
}
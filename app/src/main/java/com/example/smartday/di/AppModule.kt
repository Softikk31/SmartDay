package com.example.smartday.di

import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.main.view_models.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            getCompletedTasksUseCase = get(),
            getTasksByDateUseCase = get(),
            getTasksWithoutDateUseCase = get(),
            overdueTaskUseCase = get(),
            updateTaskUseCase = get(),
            context = get()
        )
    }

    viewModel<TaskViewModel> {
        TaskViewModel(
            getFoundTasksUseCase = get(),
            createTaskUseCase = get(),
            updateTaskUseCase = get(),
            deleteTaskUseCase = get(),
            completingTaskUseCase = get(),
            getAllTasksUseCase = get(),
            context = get()
        )
    }

    viewModel<ThemeViewModel> {
        ThemeViewModel(
            updateThemeUseCase = get(),
            updateSystemUseCase = get(),
            updatePrimaryUseCase = get(),
            getThemeUseCase = get()
        )
    }
}
package com.example.smartday.di

import com.example.smartday.presentation.main.MainViewModel
import com.example.smartday.presentation.main.TaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<MainViewModel> {
        MainViewModel(
            getCompletedTasksUseCase = get(),
            getTasksByDateUseCase = get(),
            getTasksWithoutDateUseCase = get(),
            overdueTaskUseCase = get(),
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
        )
    }
}
package com.example.smartday.di

import com.example.smartday.domain.usecase.task.CompletingTaskUseCase
import com.example.smartday.domain.usecase.task.CreateTaskUseCase
import com.example.smartday.domain.usecase.task.DeleteTaskUseCase
import com.example.smartday.domain.usecase.task.GetAllTasksUseCase
import com.example.smartday.domain.usecase.task.GetCompletedTasksUseCase
import com.example.smartday.domain.usecase.task.GetFoundTasksUseCase
import com.example.smartday.domain.usecase.task.GetTaskUseCase
import com.example.smartday.domain.usecase.task.GetTasksByDateUseCase
import com.example.smartday.domain.usecase.task.GetTasksWithoutDateUseCase
import com.example.smartday.domain.usecase.task.OverdueTaskUseCase
import com.example.smartday.domain.usecase.task.UpdateTaskUseCase
import com.example.smartday.domain.usecase.theme.GetThemeUseCase
import com.example.smartday.domain.usecase.theme.UpdatePrimaryUseCase
import com.example.smartday.domain.usecase.theme.UpdateSystemUseCase
import com.example.smartday.domain.usecase.theme.UpdateThemeUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<UpdateThemeUseCase> {
        UpdateThemeUseCase(get())
    }

    factory<UpdatePrimaryUseCase> {
        UpdatePrimaryUseCase(get())
    }

    factory<UpdateSystemUseCase> {
        UpdateSystemUseCase(get())
    }

    factory<GetThemeUseCase> {
        GetThemeUseCase(get())
    }

    factory<GetFoundTasksUseCase> {
        GetFoundTasksUseCase(get())
    }

    factory<GetAllTasksUseCase> {
        GetAllTasksUseCase(get())
    }

    factory<GetTasksByDateUseCase> {
        GetTasksByDateUseCase(get())
    }

    factory<GetCompletedTasksUseCase> {
        GetCompletedTasksUseCase(get())
    }

    factory<GetTasksWithoutDateUseCase> {
        GetTasksWithoutDateUseCase(get())
    }

    factory<GetTaskUseCase> {
        GetTaskUseCase(get())
    }

    factory<CompletingTaskUseCase> {
        CompletingTaskUseCase(get())
    }

    factory<OverdueTaskUseCase> {
        OverdueTaskUseCase(get())
    }

    factory<CreateTaskUseCase> {
        CreateTaskUseCase(get())
    }

    factory<DeleteTaskUseCase> {
        DeleteTaskUseCase(get())
    }

    factory<UpdateTaskUseCase> {
        UpdateTaskUseCase(get())
    }
}
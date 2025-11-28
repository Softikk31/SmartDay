package com.example.smartday.di

import com.example.smartday.domain.usecase.CompletingTaskUseCase
import com.example.smartday.domain.usecase.CreateTaskUseCase
import com.example.smartday.domain.usecase.DeleteTaskUseCase
import com.example.smartday.domain.usecase.GetAllTasksUseCase
import com.example.smartday.domain.usecase.GetCompletedTasksUseCase
import com.example.smartday.domain.usecase.GetFoundTasksUseCase
import com.example.smartday.domain.usecase.GetTaskUseCase
import com.example.smartday.domain.usecase.GetTasksByDateUseCase
import com.example.smartday.domain.usecase.GetTasksWithoutDateUseCase
import com.example.smartday.domain.usecase.OverdueTaskUseCase
import com.example.smartday.domain.usecase.UpdateTaskUseCase
import org.koin.dsl.module

val domainModule = module {
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
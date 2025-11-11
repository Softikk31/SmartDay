package com.example.smartday.domain.usecase

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskDomain: TaskDomain) = repository.updateTask(taskDomain = taskDomain)
}
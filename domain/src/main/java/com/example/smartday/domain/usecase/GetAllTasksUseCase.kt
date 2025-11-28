package com.example.smartday.domain.usecase

import com.example.smartday.core.models.TaskModel
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetAllTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.getAllTasks()
}
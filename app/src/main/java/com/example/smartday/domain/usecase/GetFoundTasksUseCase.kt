package com.example.smartday.domain.usecase

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetFoundTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(title: String): Flow<List<TaskDomain>> =
        repository.getFoundTasks(title)
}
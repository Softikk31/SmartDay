package com.example.smartday.domain.usecase.task

import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetFoundTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(title: String): Flow<List<TaskModel>> =
        repository.getFoundTasks(title)
}
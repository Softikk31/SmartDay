package com.example.smartday.domain.usecase

import com.example.smartday.core.models.TaskModel
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskUseCase(private val repository: TaskRepository) {
    operator fun invoke(taskId: Long): TaskModel = repository.getTask(taskId = taskId)
}
package com.example.smartday.domain.usecase.task

import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.domain.repository.TaskRepository

class GetTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Long): TaskModel = repository.getTask(taskId = taskId)
}
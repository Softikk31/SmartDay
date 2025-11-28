package com.example.smartday.domain.usecase

import com.example.smartday.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Long) {
        repository.deleteTask(taskId = taskId)
    }
}
package com.example.smartday.domain.usecase.task

import com.example.smartday.domain.repository.TaskRepository

class CompletingTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Long) {
        repository.completingTask(taskId = taskId)
    }
}
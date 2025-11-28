package com.example.smartday.domain.usecase

import com.example.smartday.domain.repository.TaskRepository

class OverdueTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Long) {
        repository.overdueTask(taskId = taskId)
    }
}
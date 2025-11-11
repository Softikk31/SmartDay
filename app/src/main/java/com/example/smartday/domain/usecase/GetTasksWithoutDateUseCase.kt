package com.example.smartday.domain.usecase

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTasksWithoutDateUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskDomain>> = repository.getTasksWithoutDate()
}
package com.example.smartday.domain.usecase

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetTasksByDateUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskDomain>> = repository.getTasksByDate()
}
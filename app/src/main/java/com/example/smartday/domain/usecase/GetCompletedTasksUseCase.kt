package com.example.smartday.domain.usecase

import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class GetCompletedTasksUseCase(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskDomain>> = repository.getCompletedTasks()
}
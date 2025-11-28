package com.example.smartday.domain.usecase

import com.example.smartday.core.models.TaskModel
import com.example.smartday.core.models.repetition.TaskRepetitionModel
import com.example.smartday.domain.repository.TaskRepository

class UpdateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel): Result<Unit> {
        when {
            (task.title.isEmpty()) -> {
                return Result.failure(
                    IllegalArgumentException("EMPTY_TITLE")
                )
            }

            ((task.date == null) and (task.notification != null)) -> {
                return Result.failure(IllegalArgumentException("NO_DATE_BUT_TIME"))
            }

            ((task.date == null) and (task.repetition != TaskRepetitionModel())) -> {
                return Result.failure(
                    IllegalArgumentException("NO_DATE_BUT_REPETITION")
                )
            }

            else -> {
                repository.updateTask(task = task)
                return Result.success(Unit)
            }
        }
    }
}
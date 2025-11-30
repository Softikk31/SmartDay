package com.example.smartday.domain.usecase.task

import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.domain.repository.TaskRepository

class CreateTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel): Result<Unit> {
        when {
            (task.title.isEmpty()) -> {
                return Result.failure(
                    IllegalArgumentException("EMPTY_TITLE")
                )
            }

            ((task.date == null) and (task.notification != null)) -> {
                return Result.failure(
                    IllegalArgumentException("NO_DATE_BUT_TIME")
                )
            }

            ((task.date == null) and (task.repetition != TaskRepetitionModel())) -> {
                return Result.failure(
                    IllegalArgumentException("NO_DATE_BUT_REPETITION")
                )
            }

            else -> {
                repository.createTask(task = task)
                return Result.success(Unit)
            }
        }
    }
}
package com.example.smartday.domain.repository

import com.example.smartday.core.models.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getFoundTasks(title: String): Flow<List<TaskModel>>
    fun getAllTasks(): Flow<List<TaskModel>>
    fun getCompletedTasks(): Flow<List<TaskModel>>
    fun getTasksByDate(): Flow<List<TaskModel>>
    fun getTasksWithoutDate(): Flow<List<TaskModel>>
    suspend fun getTask(taskId: Long): TaskModel
    suspend fun completingTask(taskId: Long)
    suspend fun overdueTask(taskId: Long)
    suspend fun createTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(taskId: Long)
}

package com.example.smartday.domain.repository

import com.example.smartday.data.sources.models.TaskData
import com.example.smartday.domain.models.TaskDomain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getFoundTasks(title: String): Flow<List<TaskDomain>>
    fun getAllTasks(): Flow<List<TaskDomain>>
    fun getCompletedTasks(): Flow<List<TaskDomain>>
    fun getTasksByDate(): Flow<List<TaskDomain>>
    fun getTasksWithoutDate(): Flow<List<TaskDomain>>
    fun getTask(taskId: Long): Flow<TaskDomain>
    suspend fun completingTask(taskId: Long)
    suspend fun overdueTask(taskId: Long)
    suspend fun createTask(taskDomain: TaskDomain)
    suspend fun updateTask(taskDomain: TaskDomain)
    suspend fun deleteTask(taskId: Long)
}

package com.example.smartday.data.sources

import com.example.smartday.data.sources.models.TaskData
import kotlinx.coroutines.flow.Flow

interface TaskLocalSource {
    fun getFoundTasks(title: String): Flow<List<TaskData>>
    fun getAllTasks(): Flow<List<TaskData>>
    fun getCompletedTasks(): Flow<List<TaskData>>
    fun getTasksByDate(): Flow<List<TaskData>>
    fun getTasksWithoutDate(): Flow<List<TaskData>>
    fun getTask(taskId: Long): Flow<TaskData>
    suspend fun completingTask(taskId: Long)
    suspend fun overdueTask(taskId: Long)
    suspend fun createTask(taskData: TaskData)
    suspend fun updateTask(taskData: TaskData)
    suspend fun deleteTask(taskId: Long)
}
package com.example.smartday.data.repository

import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.data.sources.TaskLocalSource
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val localDataSource: TaskLocalSource) : TaskRepository {
    override fun getFoundTasks(title: String): Flow<List<TaskModel>> =
        localDataSource.getFoundTasks(title = title)

    override fun getAllTasks(): Flow<List<TaskModel>> =
        localDataSource.getAllTasks()

    override fun getCompletedTasks(): Flow<List<TaskModel>> =
        localDataSource.getCompletedTasks()

    override fun getTasksByDate(): Flow<List<TaskModel>> =
        localDataSource.getTasksByDate()

    override fun getTasksWithoutDate(): Flow<List<TaskModel>> =
        localDataSource.getTasksWithoutDate()

    override suspend fun getTask(taskId: Long): TaskModel =
        localDataSource.getTask(taskId = taskId)

    override suspend fun completingTask(taskId: Long) =
        localDataSource.completingTask(taskId = taskId)

    override suspend fun overdueTask(taskId: Long) = localDataSource.overdueTask(taskId = taskId)

    override suspend fun createTask(task: TaskModel) =
        localDataSource.createTask(task = task)

    override suspend fun updateTask(task: TaskModel) =
        localDataSource.updateTask(task = task)

    override suspend fun deleteTask(taskId: Long) = localDataSource.deleteTask(taskId = taskId)
}

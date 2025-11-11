package com.example.smartday.data.repository

import com.example.smartday.data.mappers.toData
import com.example.smartday.data.mappers.toDomain
import com.example.smartday.data.sources.TaskLocalSource
import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val localDataSource: TaskLocalSource) : TaskRepository {
    override fun getFoundTasks(title: String): Flow<List<TaskDomain>> =
        localDataSource.getFoundTasks(title = title).map { taskList -> taskList.map { it.toDomain() } }

    override fun getAllTasks(): Flow<List<TaskDomain>> =
        localDataSource.getAllTasks().map { taskList -> taskList.map { it.toDomain() } }

    override fun getCompletedTasks(): Flow<List<TaskDomain>> =
        localDataSource.getCompletedTasks().map { taskList -> taskList.map {  it.toDomain() } }

    override fun getTasksByDate(): Flow<List<TaskDomain>> =
        localDataSource.getTasksByDate().map { tasksList -> tasksList.map { it.toDomain() } }

    override fun getTasksWithoutDate(): Flow<List<TaskDomain>> =
        localDataSource.getTasksWithoutDate().map { tasksList -> tasksList.map { it.toDomain() } }

    override fun getTask(taskId: Long): Flow<TaskDomain> =
        localDataSource.getTask(taskId = taskId).map { it.toDomain() }

    override suspend fun completingTask(taskId: Long) = localDataSource.completingTask(taskId = taskId)

    override suspend fun overdueTask(taskId: Long) = localDataSource.overdueTask(taskId = taskId)

    override suspend fun createTask(taskDomain: TaskDomain) = localDataSource.createTask(taskData = taskDomain.toData())

    override suspend fun updateTask(taskDomain: TaskDomain) = localDataSource.updateTask(taskData = taskDomain.toData())

    override suspend fun deleteTask(taskId: Long) = localDataSource.deleteTask(taskId = taskId)
}

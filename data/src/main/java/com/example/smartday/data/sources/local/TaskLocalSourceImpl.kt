package com.example.smartday.data.sources.local

import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.data.mappers.toEntity
import com.example.smartday.data.mappers.toModel
import com.example.smartday.data.sources.TaskLocalSource
import com.example.smartday.data.sources.local.room.TaskDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLocalSourceImpl(
    private val taskDao: TaskDao
) : TaskLocalSource {
    override fun getFoundTasks(title: String): Flow<List<TaskModel>> =
        taskDao.getFoundTasks(title = title).map { tasksList -> tasksList.map { it.toModel() } }

    override fun getAllTasks(): Flow<List<TaskModel>> =
        taskDao.getAllTasks().map { tasksList -> tasksList.map { it.toModel() } }

    override fun getCompletedTasks(): Flow<List<TaskModel>> =
        taskDao.getCompletedTasks().map { tasksList -> tasksList.map { it.toModel() } }

    override fun getTasksByDate(): Flow<List<TaskModel>> =
        taskDao.getTasksByDate().map { tasksList -> tasksList.map { it.toModel() } }

    override fun getTasksWithoutDate(): Flow<List<TaskModel>> =
        taskDao.getTasksWithoutDate().map { tasksList -> tasksList.map { it.toModel() } }

    override suspend fun getTask(taskId: Long): TaskModel =
        taskDao.getTask(taskId = taskId).toModel()

    override suspend fun completingTask(taskId: Long) =
        taskDao.completingTask(taskId = taskId)

    override suspend fun overdueTask(taskId: Long) =
        taskDao.overdueTask(taskId = taskId)

    override suspend fun createTask(task: TaskModel) =
        taskDao.createTask(task = task.toEntity())

    override suspend fun updateTask(task: TaskModel) =
        taskDao.updateTask(task = task.toEntity())

    override suspend fun deleteTask(taskId: Long) =
        taskDao.deleteTask(taskId = taskId)
}

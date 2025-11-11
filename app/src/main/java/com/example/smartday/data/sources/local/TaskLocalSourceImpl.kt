package com.example.smartday.data.sources.local

import com.example.smartday.data.mappers.toDB
import com.example.smartday.data.mappers.toData
import com.example.smartday.data.sources.TaskLocalSource
import com.example.smartday.data.sources.local.room.TaskDao
import com.example.smartday.data.sources.models.TaskData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLocalSourceImpl(
    private val taskDao: TaskDao
) : TaskLocalSource {
    override fun getFoundTasks(title: String): Flow<List<TaskData>> =
        taskDao.getFoundTasks(title = title).map { tasksList -> tasksList.map { it.toData() } }

    override fun getAllTasks(): Flow<List<TaskData>> =
        taskDao.getAllTasks().map { tasksList -> tasksList.map { it.toData() } }

    override fun getCompletedTasks(): Flow<List<TaskData>> =
        taskDao.getCompletedTasks().map { tasksList -> tasksList.map { it.toData() } }

    override fun getTasksByDate(): Flow<List<TaskData>> =
        taskDao.getTasksByDate().map { tasksList -> tasksList.map { it.toData() } }

    override fun getTasksWithoutDate(): Flow<List<TaskData>> =
        taskDao.getTasksWithoutDate().map { tasksList -> tasksList.map { it.toData() } }

    override fun getTask(taskId: Long): Flow<TaskData> =
        taskDao.getTask(taskId = taskId).map { it.toData() }

    override suspend fun completingTask(taskId: Long) =
        taskDao.completingTask(taskId = taskId)

    override suspend fun overdueTask(taskId: Long) =
        taskDao.overdueTask(taskId = taskId)

    override suspend fun createTask(taskData: TaskData) =
        taskDao.createTask(task = taskData.toDB())

    override suspend fun updateTask(taskData: TaskData) =
        taskDao.updateTask(task = taskData.toDB())

    override suspend fun deleteTask(taskId: Long) =
        taskDao.deleteTask(taskId = taskId)
}

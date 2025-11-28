package com.example.smartday.data.sources.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smartday.data.sources.local.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :title || '%'")
    fun getFoundTasks(title: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE is_completed == 1 ORDER BY date ASC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE date and is_completed == 0 ORDER BY date ASC")
    fun getTasksByDate(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE date IS NULL and is_completed == 0")
    fun getTasksWithoutDate(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTask(taskId: Long): TaskEntity

    @Query("UPDATE tasks SET is_completed = 1 WHERE id == :taskId")
    suspend fun completingTask(taskId: Long)

    @Query("UPDATE tasks SET is_overdue = 1 WHERE id == :taskId")
    suspend fun overdueTask(taskId: Long)

    @Insert
    suspend fun createTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Long)
}
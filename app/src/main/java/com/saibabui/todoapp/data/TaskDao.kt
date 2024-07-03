package com.saibabui.todoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saibabui.todoapp.data.model.TaskDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskDetails)

    @Query("UPDATE task_details_table SET taskTitle = :title, taskDescription = :description WHERE taskId = :taskId")
    suspend fun update(taskId: Int, title: String, description: String)

    @Query("DELETE FROM task_details_table WHERE taskId = :taskId")
    suspend fun delete(taskId: Int)

    @Query("SELECT * FROM task_details_table WHERE taskStatus = 0")
    fun getAllTasks(): Flow<MutableList<TaskDetails>>

    @Query("SELECT * FROM task_details_table WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Int): TaskDetails?

    @Query("UPDATE task_details_table SET taskStatus = :status WHERE taskId = :taskId")
    suspend fun completeTask(taskId: Int, status: Boolean)

    @Query("SELECT * FROM task_details_table WHERE taskStatus = :status")
    fun getCompletedTasks(status: Boolean): Flow<MutableList<TaskDetails>>

}
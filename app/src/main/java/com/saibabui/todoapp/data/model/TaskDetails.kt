package com.saibabui.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_details_table")
data class TaskDetails(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int=0,
    var taskTitle: String,
    var taskDescription: String,
    val taskPriority: String,
    var taskStatus: Boolean,
    var taskDate: Long,
)

package com.saibabui.todoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.saibabui.todoapp.data.model.TaskDetails

@Database(
    entities = [TaskDetails::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}
package com.saibabui.todoapp.domain.repository

import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.resources.Resource
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun addTodo(todo: TaskDetails) : Boolean

    suspend fun deleteTodo(taskId : Int) : Boolean

    suspend fun updateTodo(taskId: Int,title: String, description: String) :Boolean

    suspend fun getTodoById(id: Int): TaskDetails?

    suspend fun getTodos(): Flow<MutableList<TaskDetails>>?

    suspend fun completedTodo(taskId: Int) : Boolean

    suspend fun getCompletedTodos(): Flow<MutableList<TaskDetails>>
}
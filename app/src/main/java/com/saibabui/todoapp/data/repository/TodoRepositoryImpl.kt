package com.saibabui.todoapp.data.repository

import com.saibabui.todoapp.data.TaskDao
import com.saibabui.todoapp.domain.repository.TodoRepository
import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.resources.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TaskDao
) : TodoRepository {
    override suspend fun addTodo(todo: TaskDetails): Boolean {
        try {
            todoDao.insert(todo)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun deleteTodo(taskId: Int): Boolean {
        try {
            todoDao.delete(taskId)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun updateTodo(taskId: Int, title: String, description: String): Boolean {
        try {
            todoDao.update(
                taskId = taskId,
                title = title,
                description = description
            )
            return true
        } catch (e: Exception) {
            return false
        }


    }

    override suspend fun getTodoById(id: Int): TaskDetails? {
        return todoDao.getTaskById(id)
    }

    override suspend fun getTodos(): Flow<MutableList<TaskDetails>> {
        return todoDao.getAllTasks()
    }

    override suspend fun completedTodo(taskId: Int): Boolean {
        try {
            todoDao.completeTask(
                taskId = taskId)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getCompletedTodos(): Flow<MutableList<TaskDetails>> {
        return todoDao.getCompletedTasks()
    }

}
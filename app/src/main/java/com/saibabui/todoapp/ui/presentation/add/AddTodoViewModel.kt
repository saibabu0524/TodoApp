package com.saibabui.todoapp.ui.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saibabui.todoapp.domain.repository.TodoRepository
import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.resources.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _todoList = MutableStateFlow<MutableList<TaskDetails>>(mutableListOf())
    val todoList: StateFlow<List<TaskDetails>> = _todoList


    private val _completedList = MutableStateFlow<MutableList<TaskDetails>>(mutableListOf())
    val completedList: StateFlow<List<TaskDetails>> = _completedList

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                todoRepository.getTodos()?.collectLatest {
                    _todoList.value = it
                }
            }
        }
    }

    fun fetchCompletedTodos() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                todoRepository.getCompletedTodos()?.collectLatest {
                    _completedList.value = it
                }
            }
        }
    }


    private val _addTodo = MutableStateFlow<UiState<TaskDetails>>(UiState.Empty)
    val addTodo: StateFlow<UiState<TaskDetails>> = _addTodo

    private val _updateTodo = MutableStateFlow<UiState<TaskDetails>>(UiState.Empty)
    val updateTodo: StateFlow<UiState<TaskDetails>> = _updateTodo

    private val _deleteTodo = MutableStateFlow<UiState<TaskDetails>>(UiState.Empty)
    val deleteTodo: StateFlow<UiState<TaskDetails>> = _deleteTodo

    private val _getTodo = MutableStateFlow<UiState<TaskDetails>>(UiState.Empty)
    val getTodo: StateFlow<UiState<TaskDetails>> = _getTodo


    private val _completedTodo = MutableStateFlow<UiState<TaskDetails>>(UiState.Empty)
    val completedTodo: StateFlow<UiState<TaskDetails>> = _completedTodo


    fun updateTodo(
        taskId: Int,
        title: String,
        description: String,
        priority: String,
        status: Boolean,
        date: Long
    ) {
        viewModelScope.launch {
            _updateTodo.value = UiState.Loading
            try {
                val result = todoRepository.updateTodo(taskId, title, description)
                if (result) {
                    val task = TaskDetails(
                        taskId,
                        title,
                        description,
                        priority,
                        status,
                        date
                    )
                    _updateTodo.value = UiState.Success(
                        task
                    )
                } else {
                    _updateTodo.value = UiState.Failure("Something went wrong")
                }
            } catch (e: Exception) {
                _updateTodo.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }


    fun insertTodo(task: TaskDetails) {
        viewModelScope.launch {
            _addTodo.value = UiState.Loading
            try {
                val result = todoRepository.addTodo(task)
                if (result) {
                    _addTodo.value = UiState.Success(task)
                } else {
                    _addTodo.value = UiState.Failure("Something went wrong")
                }

            } catch (e: Exception) {
                _addTodo.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }


    fun deleteTodo(taskId: Int) {
        viewModelScope.launch {
            _deleteTodo.value = UiState.Loading
            try {
                val task = todoRepository.getTodoById(taskId)
                if (task == null) {
                    _deleteTodo.value = UiState.Failure("Task not found")
                    return@launch
                }
                val result = todoRepository.deleteTodo(taskId)
                if (result) {
                    _deleteTodo.value = UiState.Success(task)
                } else {
                    _deleteTodo.value = UiState.Failure("Something went wrong")
                }
            } catch (e: Exception) {
                _deleteTodo.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }


    fun completedTodo(taskId: Int) {
        viewModelScope.launch {
            _completedTodo.value = UiState.Loading
            try {
                val task = todoRepository.getTodoById(taskId)
                if (task == null) {
                    _completedTodo.value = UiState.Failure("Task not found")
                    return@launch
                }
                val result = todoRepository.completedTodo(taskId)
                if (result) {
                    _completedTodo.value = UiState.Success(task)
                } else {
                    _completedTodo.value = UiState.Failure("Something went wrong")
                    return@launch
                }
            } catch (e: Exception) {
                _completedTodo.value = UiState.Failure(e.message ?: "Unknown Error")
            }
        }
    }

    fun initialiseEmptyState() {
        _addTodo.value = UiState.Empty
    }
}

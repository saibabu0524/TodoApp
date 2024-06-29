package com.saibabui.todoapp.ui.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saibabui.todoapp.TodoCard
import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.ui.presentation.Routes
import com.saibabui.todoapp.ui.presentation.add.AddTodoViewModel
import com.saibabui.todoapp.ui.presentation.add.UiState

@Composable
fun TodoScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    addTodoViewModel: AddTodoViewModel,
    snackbarHostState: SnackbarHostState,
    onSuccessComplete: (task: TaskDetails) -> Unit,
    onSuccesfulDelete: (task: TaskDetails) -> Unit,
) {
    val todoList = addTodoViewModel.todoList.collectAsState()

    val deleteTodo = addTodoViewModel.deleteTodo.collectAsState()

    val completedTodo = addTodoViewModel.completedTodo.collectAsState()


    LaunchedEffect(key1 = deleteTodo) {
        if (deleteTodo.value is UiState.Success) {
            onSuccesfulDelete((deleteTodo.value as UiState.Success).data)
        }
        if (deleteTodo.value is UiState.Failure) {
            snackbarHostState.showSnackbar((deleteTodo.value as UiState.Failure).error)
        }
        if (deleteTodo.value is UiState.Loading) {
            snackbarHostState.showSnackbar("Deleting")
        }
    }


    LaunchedEffect(key1 = completedTodo) {
        if (completedTodo.value is UiState.Success) {
            onSuccessComplete((completedTodo.value as UiState.Success).data)
        }
        if (completedTodo.value is UiState.Failure) {
            snackbarHostState.showSnackbar((completedTodo.value as UiState.Failure).error)
        }
        if (completedTodo.value is UiState.Loading) {
            snackbarHostState.showSnackbar("Completed")
        }
    }




    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(todoList.value.size) {
            val todo = todoList.value[it]
            TodoCard(
                title = todo.taskTitle,
                description = todo.taskDescription,
                onEdit = {
                    navController.navigate(
                        Routes.EditTodoScreen.createRoute(
                            todo.taskId,
                            todo.taskTitle,
                            todo.taskDescription,
                            todo.taskPriority,
                            todo.taskStatus,
                            todo.taskDate
                        )
                    )
                },
                onDelete = {
                    addTodoViewModel.deleteTodo(todo.taskId)
                },
                onDone = {
                    addTodoViewModel.completedTodo(taskId = todo.taskId)
                })
        }
    }
}
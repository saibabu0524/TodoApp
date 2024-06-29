package com.saibabui.todoapp.ui.presentation.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.ui.theme.TodoAppTheme

@Composable
fun AddTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    innerPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    homeViewModel: AddTodoViewModel = hiltViewModel(),
    onSuccesful: (task: TaskDetails) -> Unit
) {
    val addTodoState by homeViewModel.addTodo.collectAsState()

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    LaunchedEffect(key1 = addTodoState) {
        if (addTodoState is UiState.Success) {
            navController.popBackStack()
            onSuccesful((addTodoState as UiState.Success).data)
        }
        if (addTodoState is UiState.Failure) {
            snackbarHostState.showSnackbar((addTodoState as UiState.Failure).error)
        }
        if (addTodoState is UiState.Loading) {
            snackbarHostState.showSnackbar("Adding")
        }
    }

    Column(
        modifier = modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 20.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = taskTitle,
            onValueChange = { taskTitle = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            placeholder = {
                Text(text = "Task Title")
            }
        )

        OutlinedTextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            placeholder = {
                Text(text = "Task Description")
            }
        )

        Button(
            onClick = {
                homeViewModel.insertTodo(
                    TaskDetails(
                        taskTitle = taskTitle,
                        taskDescription = taskDescription,
                        taskStatus = false,
                        taskPriority = "high",
                        taskDate = System.currentTimeMillis(),
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(bottom = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Add Task",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTaskScreenPreview() {
    TodoAppTheme {
        Surface {
            AddTaskScreen(
                navController = NavController(LocalContext.current),
                innerPadding = PaddingValues(),
                snackbarHostState = SnackbarHostState()
            ){

            }
        }
    }
}

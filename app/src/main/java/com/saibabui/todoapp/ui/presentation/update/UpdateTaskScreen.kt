package com.saibabui.todoapp.ui.presentation.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.saibabui.todoapp.data.model.TaskDetails
import com.saibabui.todoapp.ui.presentation.add.AddTodoViewModel
import com.saibabui.todoapp.ui.presentation.add.UiState
import com.saibabui.todoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    innerPadding: PaddingValues = PaddingValues(0.dp),

    taskTitle: String,
    taskDescription: String,
    taskId: Int,
    status: Boolean = false,
    priority: String,
    date: Long = 0,
    viewModel: AddTodoViewModel,
    snackbarHostState: SnackbarHostState, onSuccesful: (task: TaskDetails) -> Unit,
) {
    val updateTodoState by viewModel.updateTodo.collectAsState()
    var taskTitle by remember { mutableStateOf(taskTitle) }
    var taskDescription by remember { mutableStateOf(taskDescription) }


    LaunchedEffect(key1 = updateTodoState) {
        if (updateTodoState is UiState.Success) {
            navController.popBackStack()
            onSuccesful((updateTodoState as UiState.Success).data)
        }
        if (updateTodoState is UiState.Failure) {
            snackbarHostState.showSnackbar((updateTodoState as UiState.Failure).error)
        }
        if (updateTodoState is UiState.Loading) {
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
            value = taskTitle, onValueChange = {
                taskTitle = it
            }, colors = TextFieldDefaults.colors(
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
            value = taskDescription, onValueChange = {
                taskDescription = it
            }, colors = TextFieldDefaults.colors(
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
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 24.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium
            )
            {
                Text(
                    text = "Cancel",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(
                onClick = {
                    viewModel.updateTodo(taskId, taskTitle, taskDescription, priority, status, date)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 24.dp)
                    .padding(bottom = 16.dp),
                shape = MaterialTheme.shapes.medium
            )
            {
                Text(
                    text = "Update",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview
@Composable
private fun UpdateTaskScreenPreview() {
    TodoAppTheme {
        Surface {
            UpdateTaskScreen(
                Modifier,
                NavController(LocalContext.current),
                onSuccesful = {},
                taskTitle = "",
                taskDescription = "",
                taskId = 0,
                priority = "",
                date = 0,
                viewModel = hiltViewModel(),
                snackbarHostState = SnackbarHostState()
            )
        }
    }
}
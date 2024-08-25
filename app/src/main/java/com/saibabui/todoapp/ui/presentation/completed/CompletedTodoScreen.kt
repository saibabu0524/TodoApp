package com.saibabui.todoapp.ui.presentation.completed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saibabui.todoapp.TodoCard
import com.saibabui.todoapp.ui.presentation.Routes
import com.saibabui.todoapp.ui.presentation.add.AddTodoViewModel

@Composable
fun CompletedTodoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: AddTodoViewModel
) {
    val completedTodo by viewModel.completedList.collectAsState()
    LaunchedEffect(key1 = completedTodo) {
        viewModel.fetchCompletedTodos()
        println("completedTodo FETCHED")
    }
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        items(completedTodo.size) {
            val todo = completedTodo[it]
            CompletedTodoCard(
                title = todo.taskTitle,
                description = todo.taskDescription)
        }
    }
}


    @Composable
    fun CompletedTodoCard(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }


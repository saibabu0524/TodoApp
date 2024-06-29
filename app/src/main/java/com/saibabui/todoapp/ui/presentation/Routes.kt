package com.saibabui.todoapp.ui.presentation

sealed class Routes(val route: String) {
    data object TodoScreen : Routes("todo")
    data object AddTodoScreen : Routes("addTodo")
    data object EditTodoScreen : Routes("editTodo") {
        fun createRoute(
            taskId: Int,
            taskTitle: String,
            taskDescription: String,
            priority: String,
            status: Boolean,
            date: Long
        ) = "editTodo/$taskId/$taskTitle/$taskDescription/$priority/$status/$date"
    }

    data object CompletedTodoScreen : Routes("completedTodo")
}
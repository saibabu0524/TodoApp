package com.saibabui.todoapp.ui.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

//initializing the data class with default parameters
data class BottomNavigationItem(
    val label : String = "",
    val icon : ImageVector = Icons.Filled.Home,
    val route : String = ""
) {
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "All",
                icon = Icons.Filled.Menu,
                route = Routes.TodoScreen.route
            ),
            BottomNavigationItem(
                label = "Completed",
                icon = Icons.Filled.Done,
                route = Routes.CompletedTodoScreen.route
            ),
        )
    }
}
package com.saibabui.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.wear.compose.material.ContentAlpha
import com.saibabui.todoapp.ui.presentation.add.AddTaskScreen
import com.saibabui.todoapp.ui.presentation.BottomNavigationItem
import com.saibabui.todoapp.ui.presentation.completed.CompletedTodoScreen
import com.saibabui.todoapp.ui.presentation.Routes
import com.saibabui.todoapp.ui.presentation.add.AddTodoViewModel
import com.saibabui.todoapp.ui.presentation.main.TodoScreen
import com.saibabui.todoapp.ui.presentation.update.UpdateTaskScreen
import com.saibabui.todoapp.ui.theme.TodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            val navController = rememberNavController()
            var navigationSelectedItem by remember {
                mutableStateOf(0)
            }
            val homeViewModel: AddTodoViewModel by viewModels()

            var showSnackBarMessage by remember {
                mutableStateOf("")
            }

            val snackBarHostState = SnackbarHostState()
            // Create a state to hold the current screen route
            val currentScreen by navController.currentBackStackEntryAsState()
            val screen = getCurrentScreen(currentScreen)
            TodoAppTheme {
                Surface {
                    LaunchedEffect(key1 = showSnackBarMessage) {
                        snackBarHostState.showSnackbar("$showSnackBarMessage  successfully")
                    }
                    Scaffold(snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }, modifier = Modifier.fillMaxSize(), topBar = {
                        when (screen) {
                            Routes.TodoScreen -> MainScreenTopAppBar()
                            Routes.AddTodoScreen -> AddTodoScreenTopAppBar(navController = navController)
                            Routes.CompletedTodoScreen -> MainScreenTopAppBar()
                            Routes.EditTodoScreen -> AddTodoScreenTopAppBar(navController = navController)
                        }
                    }, floatingActionButton = {
                        when (screen) {
                            Routes.AddTodoScreen -> Unit
                            Routes.CompletedTodoScreen -> {
                                CustomFab(navController = navController)
                            }

                            Routes.EditTodoScreen -> Unit
                            Routes.TodoScreen -> {
                                CustomFab(navController = navController)
                            }

                        }

                    }, bottomBar = {
                        NavigationBar {
                            //getting the list of bottom navigation items for our data class
                            BottomNavigationItem().bottomNavigationItems()
                                .forEachIndexed { index, navigationItem ->
                                    //iterating all items with their respective indexes
                                    NavigationBarItem(
                                        selected = index == navigationSelectedItem,
                                        label = {
                                            Text(navigationItem.label)
                                        },
                                        icon = {
                                            Icon(
                                                navigationItem.icon,
                                                contentDescription = navigationItem.label
                                            )
                                        },
                                        onClick = {
                                            navigationSelectedItem = index
                                            navController.navigate(navigationItem.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                        }
                    }) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Routes.TodoScreen.route
                        ) {
                            composable(route = Routes.TodoScreen.route) {
                                TodoScreen(
                                    navController = navController,
                                    paddingValues = innerPadding,
                                    snackbarHostState = snackBarHostState,
                                    addTodoViewModel = homeViewModel,
                                    onSuccessComplete = {
                                        showSnackBarMessage =
                                            "${it.taskTitle} completed successfully"
                                    }
                                ) { todo ->
                                    showSnackBarMessage =
                                        "${todo.taskTitle} deleted successfully"
                                }
                            }
                            composable(route = Routes.AddTodoScreen.route) {
                                AddTaskScreen(
                                    navController = navController,
                                    innerPadding = innerPadding,
                                    snackbarHostState = snackBarHostState,
                                    homeViewModel = homeViewModel
                                ) { todo ->
                                    showSnackBarMessage =
                                        "${todo.taskTitle} added successfully added"
                                }
                            }
                            composable(
                                route = Routes.EditTodoScreen.route + "/{taskId}/{taskTitle}/{taskDescription}/{priority}/{status}/{date}",
                                arguments = listOf(navArgument("taskId") {
                                    type = NavType.IntType
                                }, navArgument("taskTitle") {
                                    type = NavType.StringType
                                }, navArgument("taskDescription") {
                                    type = NavType.StringType
                                }, navArgument("priority") {
                                    type = NavType.StringType
                                }, navArgument("status") {
                                    type = NavType.BoolType
                                }, navArgument("date") {
                                    type = NavType.LongType
                                }
                                )
                            ) { it ->
                                val taskId = it.arguments?.getInt("taskId")
                                val taskTitle = it.arguments?.getString("taskTitle")
                                val taskDescription = it.arguments?.getString("taskDescription")
                                val priority = it.arguments?.getString("priority")
                                val status = it.arguments?.getBoolean("status")
                                val date = it.arguments?.getLong("date")
                                UpdateTaskScreen(
                                    navController = navController,
                                    innerPadding = innerPadding,

                                    taskTitle = taskTitle ?: "",
                                    taskDescription = taskDescription ?: "",
                                    taskId = taskId ?: 0,
                                    priority = priority ?: "",
                                    status = status ?: false,
                                    date = date ?: 0,
                                    viewModel = homeViewModel,
                                    snackbarHostState = snackBarHostState
                                ) { taskDetails ->
                                    showSnackBarMessage =
                                        "${taskDetails.taskTitle} updated successfully"
                                }
                            }
                            composable(route = Routes.CompletedTodoScreen.route) {
                                CompletedTodoScreen(
                                    navController = navController,
                                    paddingValues = innerPadding,
                                    viewModel = homeViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

val addTodo: (navController: NavController) -> Unit = {
    it.navigate(Routes.AddTodoScreen.route)
}


@Composable
fun TodoCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDone: () -> Unit
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onEdit() }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                IconButton(onClick = { onDelete() }) {

                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDone() }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }


    }
}

@Preview
@Composable
private fun TodoCardPreview() {
    TodoAppTheme {
        Surface {
            TodoCard(
                title = "Title",
                description = "Description",
                onEdit = {},
                onDelete = {},
                onDone = {})
        }
    }
}

@Composable
fun RowScope.BottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = "TODO APP",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.calender_date_icon),
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreenTopAppBar(navController: NavController, modifier: Modifier = Modifier) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                text = "TODO APP",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.calender_date_icon),
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )

}

@Composable
fun getCurrentScreen(backStackEntry: NavBackStackEntry?): Routes {
    return when (backStackEntry?.destination?.route) {
        Routes.TodoScreen.route -> Routes.TodoScreen
        Routes.AddTodoScreen.route -> Routes.AddTodoScreen
        Routes.EditTodoScreen.route -> Routes.EditTodoScreen
        Routes.CompletedTodoScreen.route -> Routes.CompletedTodoScreen
        else -> Routes.TodoScreen
    }
}

@Composable
fun CustomFab(modifier: Modifier = Modifier, navController: NavController) {
    FloatingActionButton(onClick = { addTodo(navController) }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Back"
        )
    }
}

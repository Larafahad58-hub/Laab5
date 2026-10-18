package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object PlannerHomeRouteObj

@Serializable
data class TaskDetailRouteObj(val title: String)

data class PlannerUiState(
    val greeting: String = "Hello, Student",
    val tasks: List<String> = listOf("Lab 5 Report", "Quiz 2 Preparation", "Project Demo")
)

data class TaskDetailUiState(
    val title: String = ""
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudyPlannerApp()
            }
        }
    }
}

@Composable
fun StudyPlannerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PlannerHomeRouteObj
    ) {
        composable<PlannerHomeRouteObj> {
            PlannerHomeRoute(
                onTaskClick = { taskTitle ->
                    navController.navigate(TaskDetailRouteObj(title = taskTitle))
                }
            )
        }
        composable<TaskDetailRouteObj> { backStackEntry ->
            val route: TaskDetailRouteObj = backStackEntry.toRoute()
            TaskDetailRoute(
                title = route.title,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun PlannerHomeRoute(
    onTaskClick: (String) -> Unit
) {
    var uiState by remember { mutableStateOf(PlannerUiState()) }

    PlannerHomeScreen(
        uiState = uiState,
        onTaskClick = onTaskClick
    )
}

@Composable
fun PlannerHomeScreen(
    uiState: PlannerUiState,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.greeting,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select a Task:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            uiState.tasks.forEach { task ->
                Button(
                    onClick = { onTaskClick(task) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = task)
                }
            }
        }
    }
}

@Composable
fun TaskDetailRoute(
    title: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState = TaskDetailUiState(title = title)

    TaskDetailScreen(
        uiState = uiState,
        onShareClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Today I'm studying: ${uiState.title}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
        onBackClick = onBackClick
    )
}

@Composable
fun TaskDetailScreen(
    uiState: TaskDetailUiState,
    onShareClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                OutlinedButton(onClick = onBackClick) {
                    Text(text = "Back")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = onShareClick) {
                    Text(text = "Share")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Lab Report Task")
@Composable
fun TaskDetailScreenPreview1() {
    MaterialTheme {
        TaskDetailScreen(
            uiState = TaskDetailUiState(title = "Lab 5 Report"),
            onShareClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Quiz Prep Task")
@Composable
fun TaskDetailScreenPreview2() {
    MaterialTheme {
        TaskDetailScreen(
            uiState = TaskDetailUiState(title = "Quiz 2 Preparation"),
            onShareClick = {},
            onBackClick = {}
        )
    }
}

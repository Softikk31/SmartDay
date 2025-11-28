package com.example.smartday.ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.models.states.TaskListItem
import com.example.smartday.ui.ui.navigation.Screen
import com.example.smartday.ui.utils.toDisplayString
import java.util.Locale

@Composable
fun ItemTasks(
    value: TaskListItem,
    lifecycleOwner: LifecycleOwner,
    taskViewModel: TaskViewModel,
    navController: NavHostController
) {
    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

    val context = LocalContext.current

    when (value) {
        is TaskListItem.Title.IntTitle -> {
            Text(
                text = stringResource(value.title),
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        is TaskListItem.Title.DateTitle -> {
            Text(
                text = value.title.toDisplayString(locale, context),
                style = MaterialTheme.typography.bodySmall
                    .copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        is TaskListItem.TaskList -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                value.taskList.forEach { task ->
                    key(task.id) {
                        TaskCard(
                            task = task,
                            taskViewModel = taskViewModel,
                            onLongClick = {
                                taskViewModel.deleteMode(true)
                                taskViewModel.selectTaskId(task.id)
                            }
                        ) {
                            val currentState = lifecycleOwner.lifecycle.currentState
                            if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                                navController.navigate(Screen.Task)
                            }
                        }
                    }
                }
            }
        }
    }
}

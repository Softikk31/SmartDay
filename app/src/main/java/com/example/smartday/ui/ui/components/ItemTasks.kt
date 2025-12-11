package com.example.smartday.ui.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.models.states.TaskListItem
import com.example.smartday.ui.ui.navigation.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ItemTasks(
    value: TaskListItem,
    lifecycleOwner: LifecycleOwner,
    taskViewModel: TaskViewModel,
    navController: NavHostController
) {
    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

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
                text = value.title.toDisplayStringCustom(locale),
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


fun LocalDate.toDisplayStringCustom(locale: Locale): String {
    val formatter = if (locale.language == "ru") {
        DateTimeFormatter.ofPattern("EEEE, d MMM", locale)
    } else {
        DateTimeFormatter.ofPattern("EEEE, MMM d", locale)
    }
    val result = this.format(formatter)
    return if (locale.language == "ru") {
        result.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(locale) else it.toString()
        }
    } else {
        result
    }
}
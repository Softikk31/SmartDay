package com.example.smartday.ui.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.components.CustomFloatActionButton
import com.example.smartday.ui.ui.components.CustomScaffoldTopBar
import com.example.smartday.ui.ui.components.ItemTasks
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    taskViewModel: TaskViewModel
) {
    val allTasksListItem by viewModel.allTasksListItem.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    BackHandler {
        if (taskDeleteMode.isDeleting) {
            taskViewModel.onDismissDeleteMode()
        }
    }

    CustomScaffoldTopBar(
        topBar = {
            AnimatedVisibility(
                visible = taskDeleteMode.isDeleting,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomTopBar(
                    icon = ImageVector.vectorResource(R.drawable.ic_x),
                    text = pluralStringResource(
                        R.plurals.selected_task_title,
                        taskDeleteMode.selectedIds.size,
                        taskDeleteMode.selectedIds.size
                    ),
                    checked = taskDeleteMode.selectAll,
                    onCheckedChange = { check ->
                        taskViewModel.checkedSelectAll(check)
                        taskViewModel.selectAllTaskId()
                    }
                ) {
                    taskViewModel.onDismissDeleteMode()
                }
            }

            AnimatedVisibility(
                visible = !taskDeleteMode.isDeleting,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomTopBar(
                    text = stringResource(R.string.title_tasks)
                ) {
                    Icon(
                        modifier = Modifier
                            .absoluteOffset(x = 2.dp)
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    navController.navigate(Screen.Search)
                                }),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !taskDeleteMode.isDeleting,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CustomFloatActionButton(
                    onClick = {
                        val currentState = lifecycleOwner.lifecycle.currentState
                        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            navController.navigate(Screen.Task)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (allTasksListItem.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 126.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_tasks_found_text),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(allTasksListItem) { value ->
                            ItemTasks(value, lifecycleOwner, taskViewModel, navController)
                        }
                        item {
                            Spacer(modifier = Modifier.height(112.dp))
                        }
                    }
                }
            }
        }
    }
}

package com.example.smartday.ui.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.models.states.TaskFoundState
import com.example.smartday.ui.ui.components.CustomScaffoldTopBar
import com.example.smartday.ui.ui.components.TaskCard
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(400)
        focusRequester.requestFocus()
    }

    val textFieldState = rememberTextFieldState(initialText = "")

    val title by remember(textFieldState.text) { mutableStateOf(textFieldState.text) }

    val tasksFound by taskViewModel.getFoundTasks(title.toString()).collectAsState(TaskFoundState())

    BackHandler {
        val currentState = lifecycleOwner.lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            navController.popBackStack()
        }
    }

    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                icon = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                onClick = {
                    val currentState = lifecycleOwner.lifecycle.currentState
                    if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navController.popBackStack()
                        taskViewModel.onDismissDeleteAndEditTask()
                    }
                }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .focusRequester(focusRequester),
                    state = textFieldState,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.title_search_bar),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        navController.navigate(Screen.Search)
                                    }),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null
                        )
                    },
                    contentPadding = TextFieldDefaults.contentPaddingWithLabel(8.dp, 8.dp),
                    colors = colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            0.1f
                        ),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    lineLimits = TextFieldLineLimits.SingleLine
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tasksFound.matchesTitle.forEach { (matchesTitle, taskList) ->
                    items(taskList) { task ->
                        key(task.id) {
                            TaskCard(
                                task = task,
                                taskViewModel = taskViewModel,
                                matchesTitle = matchesTitle,
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
                item {
                    Spacer(modifier = Modifier.height(112.dp))
                }
            }
        }
    }
}

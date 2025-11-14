package com.example.smartday.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.presentation.main.TaskViewModel
import com.example.smartday.presentation.ui.components.CustomFloatActionButton
import com.example.smartday.presentation.ui.components.CustomScaffoldTopBar
import com.example.smartday.presentation.ui.components.bars.CustomTopBar
import com.example.smartday.presentation.ui.components.dialogs.DateTimeDialog
import com.example.smartday.presentation.utils.toDisplayString
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun TaskScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {
    val state by taskViewModel.stateAddAndEditTask.collectAsState()
    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

    val textFieldState = rememberTextFieldState(initialText = state.title)

    LaunchedEffect(textFieldState.text) {
        taskViewModel.onTitleChange(textFieldState.text.toString())
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(400)
        focusRequester.requestFocus()
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    BackHandler {
        val currentState = lifecycleOwner.lifecycle.currentState
        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            navController.popBackStack()
            taskViewModel.onDismissDeleteAndEditTask()
        }
    }

    DateTimeDialog(state = state, taskViewModel = taskViewModel, locale = locale)

    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                icon = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                text = stringResource(if (state.id != null) R.string.title_edit_task else R.string.title_create_task)
            ) {
                val currentState = lifecycleOwner.lifecycle.currentState
                if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    navController.popBackStack()
                    taskViewModel.onDismissDeleteAndEditTask()
                }
            }
        }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            CustomFloatActionButton(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Alignment.BottomEnd)
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
                enabled = state.title.trim().isNotEmpty(),
                colorContainer = MaterialTheme.colorScheme.primary.copy(
                    if (state.title.trim().isNotEmpty()) 1f else 0.5f
                )
            ) {
                if (state.id != null) {
                    taskViewModel.editTask()
                    navController.popBackStack()
                } else {
                    taskViewModel.createTask()
                    navController.popBackStack()
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .focusRequester(focusRequester),
                    state = textFieldState,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.task_title), style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    colors = colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f)
                        )
                    ),
                    shape = RoundedCornerShape(4.dp),
                    lineLimits = TextFieldLineLimits.SingleLine
                )

                Column {
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                    )
                    ButtonCustomiseTask(
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        title = stringResource(R.string.task_create_button_title_date),
                        value = state.date?.toDisplayString(locale, LocalContext.current)
                            ?: stringResource(R.string.no_date)
                    ) {
                        taskViewModel.dateSelection()
                    }
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                    )
                    ButtonCustomiseTask(
                        icon = ImageVector.vectorResource(R.drawable.ic_bell),
                        title = stringResource(R.string.task_create_button_title_reminder_time),
                        value = state.time?.toString() ?: stringResource(R.string.no_time)
                    ) {
                        if (state.date != null) {
                            taskViewModel.timeSelection()
                        } else {
                            taskViewModel.dateSelection()
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                    )
                    ButtonCustomiseTask(
                        icon = ImageVector.vectorResource(R.drawable.ic_bell),
                        title = stringResource(R.string.task_create_button_title_reminder_time),
                        value = state.time?.toString() ?: stringResource(R.string.no_time)
                    ) {
                        if (state.date != null) {
                            taskViewModel.timeSelection()
                        } else {
                            taskViewModel.dateSelection()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ButtonCustomiseTask(
    modifier: Modifier = Modifier, icon: ImageVector, title: String, value: String, onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(23.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
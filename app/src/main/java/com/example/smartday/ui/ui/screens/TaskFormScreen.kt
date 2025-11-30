package com.example.smartday.ui.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionCustom
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.core.models.task.repetition.values.DaysOfWeekRepetition
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.components.CustomFloatActionButton
import com.example.smartday.ui.ui.components.CustomScaffoldTopBar
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.components.dialogs.TaskFormDialog
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.Red
import com.example.smartday.ui.ui.theme.Yellow
import com.example.smartday.ui.utils.toDisplayString
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TaskScreen(
    navController: NavHostController, taskViewModel: TaskViewModel
) {
    val state by taskViewModel.stateTaskForm.collectAsState()
    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

    val context = LocalContext.current

    val toastEmptyTitle = Toast.makeText(
        context, stringResource(R.string.toast_warning_empty_title), Toast.LENGTH_SHORT
    )

    val toastNullTime = Toast.makeText(
        context,
        stringResource(R.string.toast_warning_null_date_and_not_null_time),
        Toast.LENGTH_SHORT
    )

    val toastNullRepetition = Toast.makeText(
        context,
        stringResource(R.string.toast_warning_null_date_and_not_null_repetition),
        Toast.LENGTH_SHORT
    )

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

    TaskFormDialog(state = state, taskViewModel = taskViewModel, locale = locale)

    CustomScaffoldTopBar(topBar = {
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
    }, floatingActionButton = {
        CustomFloatActionButton(
            modifier = Modifier.imePadding()
        ) {
            if ((state.title.isNotEmpty() and !((state.date == null) and
                        (state.time != null)) and !((state.date == null) and
                        (state.repetition != TaskRepetitionModel())))
            ) {
                if (state.id != null) {
                    taskViewModel.editTask(context = context)
                    navController.popBackStack()
                } else {
                    taskViewModel.createTask(context = context)
                    navController.popBackStack()
                }
                taskViewModel.onDismissDeleteAndEditTask()
            } else if (state.title.isEmpty()) {
                toastEmptyTitle.show()
            } else if ((state.date == null) and (state.time != null)) {
                toastNullTime.show()
            } else if ((state.date == null) and (state.time != TaskRepetitionModel())) {
                toastNullRepetition.show()
            }
        }
    }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
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
                            text = stringResource(R.string.task_title),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
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
                    shape = RoundedCornerShape(4.dp),
                    lineLimits = TextFieldLineLimits.SingleLine
                )

                ButtonCustomiseTaskBar({
                    ButtonCustomiseTask(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = ImageVector.vectorResource(R.drawable.ic_calendar),
                        title = stringResource(R.string.task_create_button_title_date),
                        value = state.date?.toDisplayString(locale, LocalContext.current)
                            ?: stringResource(R.string.none)
                    ) {
                        taskViewModel.dateSelection()
                    }
                }, {
                    ButtonCustomiseTask(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = ImageVector.vectorResource(R.drawable.ic_bell),
                        title = stringResource(R.string.task_create_button_title_reminder_time),
                        value = state.time?.toString() ?: stringResource(R.string.none)
                    ) {
                        taskViewModel.timeSelection()
                    }
                }, {
                    ButtonCustomiseTask(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = ImageVector.vectorResource(R.drawable.ic_round_refresh),
                        title = stringResource(R.string.task_create_button_title_repetition),
                        value = when (val type = state.repetition.type) {
                            is TaskTypeRepetition.OnSystemTypeRepetition -> {
                                when (type.enumClass) {
                                    TaskTypeRepetitionSystem.EVERY_DAY ->
                                        stringResource(
                                            R.string.repeat_every_day
                                        )

                                    TaskTypeRepetitionSystem.EVERY_WEEK_ON -> {
                                        val dayOfWeek = state.date?.dayOfWeek?.getDisplayName(
                                            TextStyle.SHORT_STANDALONE, locale
                                        ) ?: LocalDate.now().dayOfWeek.getDisplayName(
                                            TextStyle.SHORT_STANDALONE, locale
                                        )
                                        stringResource(
                                            R.string.repeat_every_week_on,
                                            dayOfWeek
                                        )
                                    }

                                    TaskTypeRepetitionSystem.EVERY_WORKDAY ->
                                        stringResource(R.string.repeat_every_workday)

                                    TaskTypeRepetitionSystem.EVERY_MONTH_DATE -> {
                                        val dayOfMonth =
                                            state.date?.dayOfMonth ?: LocalDate.now().dayOfMonth
                                        stringResource(
                                            R.string.repeat_every_month_on,
                                            dayOfMonth
                                        )
                                    }

                                    TaskTypeRepetitionSystem.EVERY_YEAR_DATE -> {
                                        val date = state.date ?: LocalDate.now()
                                        val monthName = date.month.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            locale
                                        )
                                        stringResource(
                                            R.string.repeat_every_year_on,
                                            "${date.dayOfMonth} $monthName"
                                        )
                                    }

                                    else -> stringResource(R.string.none)
                                }
                            }

                            is TaskTypeRepetition.OnCustomTypeRepetition -> {
                                val counter = state.repetition.counter
                                when (type.enumClass) {
                                    TaskTypeRepetitionCustom.DAY ->
                                        if (counter == 1) {
                                            stringResource(
                                                R.string.repeat_every_day
                                            )
                                        } else {
                                            pluralStringResource(
                                                R.plurals.repeat_every_x_days_on,
                                                counter,
                                                counter
                                            )
                                        }

                                    TaskTypeRepetitionCustom.WEEK -> {
                                        val daysRepetition =
                                            (state.repetition.value as? DaysOfWeekRepetition)?.value
                                                ?: emptyList()

                                        val daysWeek = daysRepetition.joinToString(", ") { day ->
                                            day.getDisplayName(TextStyle.SHORT_STANDALONE, locale)
                                        }

                                        if (counter == 1) {
                                            stringResource(
                                                R.string.repeat_every_week_on,
                                                daysWeek
                                            )
                                        } else {
                                            pluralStringResource(
                                                R.plurals.repeat_every_x_weeks_on,
                                                counter,
                                                counter,
                                                daysWeek
                                            )
                                        }
                                    }

                                    TaskTypeRepetitionCustom.MONTH -> {
                                        val dayOfMonth =
                                            state.date?.dayOfMonth ?: LocalDate.now().dayOfMonth

                                        if (counter == 1) {
                                            stringResource(
                                                R.string.repeat_every_month_on,
                                                dayOfMonth
                                            )
                                        } else {
                                            pluralStringResource(
                                                R.plurals.repeat_every_x_months_on,
                                                counter,
                                                counter,
                                                dayOfMonth
                                            )
                                        }
                                    }

                                    TaskTypeRepetitionCustom.YEAR -> {
                                        val date = state.date ?: LocalDate.now()
                                        val monthName = date.month.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            locale
                                        )

                                        if (counter == 1) {
                                            stringResource(
                                                R.string.repeat_every_year_on,
                                                "${date.dayOfMonth} $monthName"
                                            )
                                        } else {
                                            pluralStringResource(
                                                R.plurals.repeat_every_x_years_on,
                                                counter,
                                                counter,
                                                "${date.dayOfMonth} $monthName"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        taskViewModel.repetitionSelection()
                    }

                }, {
                    ButtonCustomiseTask(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        icon = ImageVector.vectorResource(R.drawable.ic_flag),
                        iconColor = when (state.priority) {
                            TaskPriority.NULL -> MaterialTheme.colorScheme.onSurfaceVariant
                            TaskPriority.LOW -> Blue
                            TaskPriority.MEDIUM -> Yellow
                            TaskPriority.HIGH -> Red
                        },
                        title = stringResource(R.string.task_create_button_title_priority),
                        value = when (state.priority) {
                            TaskPriority.NULL -> stringResource(R.string.none)
                            TaskPriority.LOW -> stringResource(R.string.low_priority)
                            TaskPriority.MEDIUM -> stringResource(R.string.medium_priority)
                            TaskPriority.HIGH -> stringResource(R.string.high_priority)
                        }
                    ) {
                        taskViewModel.prioritySelection()
                    }
                })
            }
        }
    }
}


@Composable
fun ButtonCustomiseTaskBar(
    vararg content: @Composable () -> Unit
) {
    Column {
        content.forEach { item ->
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
            )
            item()
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
        )
    }
}

@Composable
fun ButtonCustomiseTask(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    title: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(62.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(23.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )

                Box(
                    modifier = Modifier.width(100.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                value?.let {
                    Box(
                        modifier = Modifier.width(120.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Icon(
                    modifier = Modifier.size(23.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_incomplete),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
package com.example.smartday.ui.ui.components.bottom_sheet

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionCustom
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.core.models.task.repetition.values.DaysOfWeekRepetition
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.components.dialogs.TaskFormDialog
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.RedPrimary
import com.example.smartday.ui.ui.theme.Yellow
import com.example.smartday.ui.utils.toDisplayString
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    showBottomSheet: Boolean, taskViewModel: TaskViewModel, onDismissRequest: () -> Unit = {}
) {

    val sheetState = rememberModalBottomSheetState()
    val state by taskViewModel.stateTaskForm.collectAsState()

    val titleTextFieldState =
        key(showBottomSheet) { rememberTextFieldState(initialText = state.title) }
    val descriptionTextFieldState = key(showBottomSheet) { rememberTextFieldState() }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(showBottomSheet) {
        delay(400)
        focusRequester.requestFocus()
    }

    LaunchedEffect(titleTextFieldState.text) {
        taskViewModel.onTitleChange(titleTextFieldState.text.toString())
    }

    LaunchedEffect(descriptionTextFieldState.text) {
        taskViewModel.onDescriptionChange(descriptionTextFieldState.text.toString())
    }

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

    TaskFormDialog(state = state, taskViewModel = taskViewModel, locale = locale)

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            dragHandle = {}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            state = titleTextFieldState,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            lineLimits = TextFieldLineLimits.SingleLine,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorator = TextFieldDefaults.decorator(
                                state = titleTextFieldState,
                                enabled = true,
                                lineLimits = TextFieldLineLimits.SingleLine,
                                outputTransformation = null,
                                interactionSource = remember { MutableInteractionSource() },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.task_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                contentPadding = PaddingValues(0.dp),
                                colors = colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    selectionColors = TextSelectionColors(
                                        handleColor = MaterialTheme.colorScheme.primary,
                                        backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                            0.4f
                                        )
                                    )
                                )
                            )
                        )

                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = descriptionTextFieldState,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            lineLimits = TextFieldLineLimits.SingleLine,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorator = TextFieldDefaults.decorator(
                                state = descriptionTextFieldState,
                                enabled = true,
                                lineLimits = TextFieldLineLimits.SingleLine,
                                outputTransformation = null,
                                interactionSource = remember { MutableInteractionSource() },
                                placeholder = {
                                    Text(
                                        text = stringResource(R.string.task_description),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = 16.sp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                contentPadding = PaddingValues(0.dp),
                                colors = colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    selectionColors = TextSelectionColors(
                                        handleColor = MaterialTheme.colorScheme.primary,
                                        backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                            0.4f
                                        )
                                    )
                                )
                            )
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            item {
                                TaskCustomiseButton(
                                    title = state.date?.toDisplayString(
                                        locale = locale, context = LocalContext.current
                                    )
                                        ?: stringResource(R.string.task_create_button_title_date),
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_calendar)
                                ) {
                                    taskViewModel.dateSelection()
                                }
                            }
                            item {
                                TaskCustomiseButton(
                                    title = state.time?.toString()
                                        ?: stringResource(R.string.task_create_button_title_reminder),
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_clock)
                                ) {
                                    taskViewModel.timeSelection()
                                }
                            }
                            item {
                                TaskCustomiseButton(
                                    title = when (val type =
                                        state.repetition.type) {
                                        is TaskTypeRepetition.OnSystemTypeRepetition -> {
                                            when (type.enumClass) {
                                                TaskTypeRepetitionSystem.EVERY_DAY -> stringResource(
                                                    R.string.repeat_every_day
                                                )

                                                TaskTypeRepetitionSystem.EVERY_WEEK_ON -> {
                                                    val dayOfWeek =
                                                        state.date?.dayOfWeek?.getDisplayName(
                                                            TextStyle.SHORT_STANDALONE,
                                                            locale
                                                        )
                                                            ?: LocalDate.now().dayOfWeek.getDisplayName(
                                                                TextStyle.SHORT_STANDALONE,
                                                                locale
                                                            )
                                                    stringResource(
                                                        R.string.repeat_every_week_on,
                                                        dayOfWeek
                                                    )
                                                }

                                                TaskTypeRepetitionSystem.EVERY_WORKDAY -> stringResource(
                                                    R.string.repeat_every_workday
                                                )

                                                TaskTypeRepetitionSystem.EVERY_MONTH_DATE -> {
                                                    val dayOfMonth = state.date?.dayOfMonth
                                                        ?: LocalDate.now().dayOfMonth
                                                    stringResource(
                                                        R.string.repeat_every_month_on,
                                                        dayOfMonth
                                                    )
                                                }

                                                TaskTypeRepetitionSystem.EVERY_YEAR_DATE -> {
                                                    val date = state.date ?: LocalDate.now()
                                                    val monthName =
                                                        date.month.getDisplayName(
                                                            TextStyle.SHORT_STANDALONE,
                                                            locale
                                                        )
                                                    stringResource(
                                                        R.string.repeat_every_year_on,
                                                        "${date.dayOfMonth} $monthName"
                                                    )
                                                }

                                                else -> stringResource(R.string.task_create_button_title_repeat)
                                            }
                                        }


                                        is TaskTypeRepetition.OnCustomTypeRepetition -> {
                                            val counter = state.repetition.counter
                                            when (type.enumClass) {
                                                TaskTypeRepetitionCustom.DAY -> if (counter.toInt() == 1) {
                                                    stringResource(
                                                        R.string.repeat_every_day
                                                    )
                                                } else {
                                                    pluralStringResource(
                                                        R.plurals.repeat_every_x_days_on,
                                                        counter.toInt(),
                                                        counter
                                                    )
                                                }

                                                TaskTypeRepetitionCustom.WEEK -> {
                                                    val daysRepetition =
                                                        (state.repetition.value as? DaysOfWeekRepetition)?.value
                                                            ?: emptyList()

                                                    val daysWeek =
                                                        daysRepetition.joinToString(", ") { day ->
                                                            day.getDisplayName(
                                                                TextStyle.SHORT_STANDALONE,
                                                                locale
                                                            )
                                                        }

                                                    if (counter.toInt() == 1) {
                                                        stringResource(
                                                            R.string.repeat_every_week_on,
                                                            daysWeek
                                                        )
                                                    } else {
                                                        pluralStringResource(
                                                            R.plurals.repeat_every_x_weeks_on,
                                                            counter.toInt(),
                                                            counter,
                                                            daysWeek
                                                        )
                                                    }
                                                }

                                                TaskTypeRepetitionCustom.MONTH -> {
                                                    val dayOfMonth = state.date?.dayOfMonth
                                                        ?: LocalDate.now().dayOfMonth

                                                    if (counter.toInt() == 1) {
                                                        stringResource(
                                                            R.string.repeat_every_month_on,
                                                            dayOfMonth
                                                        )
                                                    } else {
                                                        pluralStringResource(
                                                            R.plurals.repeat_every_x_months_on,
                                                            counter.toInt(),
                                                            counter,
                                                            dayOfMonth
                                                        )
                                                    }
                                                }

                                                TaskTypeRepetitionCustom.YEAR -> {
                                                    val date = state.date ?: LocalDate.now()
                                                    val monthName =
                                                        date.month.getDisplayName(
                                                            TextStyle.SHORT_STANDALONE,
                                                            locale
                                                        )

                                                    if (counter.toInt() == 1) {
                                                        stringResource(
                                                            R.string.repeat_every_year_on,
                                                            "${date.dayOfMonth} $monthName"
                                                        )
                                                    } else {
                                                        pluralStringResource(
                                                            R.plurals.repeat_every_x_years_on,
                                                            counter.toInt(),
                                                            counter,
                                                            "${date.dayOfMonth} $monthName"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_repeat)) {
                                    taskViewModel.repetitionSelection()
                                }
                            }
                            item {
                                TaskCustomiseButton(
                                    title = when (state.priority) {
                                        TaskPriority.NULL -> stringResource(R.string.task_create_button_title_priority)
                                        TaskPriority.LOW -> stringResource(R.string.low_priority)
                                        TaskPriority.MEDIUM -> stringResource(R.string.medium_priority)
                                        TaskPriority.HIGH -> stringResource(R.string.high_priority)
                                    },
                                    contentColor = when (state.priority) {
                                        TaskPriority.NULL -> MaterialTheme.colorScheme.outline
                                        TaskPriority.LOW -> Blue
                                        TaskPriority.MEDIUM -> Yellow
                                        TaskPriority.HIGH -> RedPrimary
                                    },
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_flag)
                                ) {
                                    taskViewModel.prioritySelection()
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        0.1f
                                    )
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .padding(vertical = 8.dp)
                            .height(40.dp)
                            .width(48.dp),
                        contentPadding = PaddingValues(vertical = 6.dp, horizontal = 10.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.surface
                        ),
                        onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val alarmManager =
                                    context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                if (!alarmManager.canScheduleExactAlarms()) {
                                    val intent =
                                        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                                            data = Uri.fromParts(
                                                "package", context.packageName, null
                                            )
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }

                                    context.startActivity(intent)
                                } else {
                                    if ((state.title.isNotEmpty() and !((state.date == null) and (state.time != null)) and !((state.date == null) and (state.repetition != TaskRepetitionModel())))) {
                                        if (state.id != null) {
                                            taskViewModel.editTask(context = context)
                                            onDismissRequest()
                                        } else {
                                            taskViewModel.createTask(context = context)
                                            onDismissRequest()
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
                            } else {
                                if ((state.title.isNotEmpty() and !((state.date == null) and (state.time != null)) and !((state.date == null) and (state.repetition != TaskRepetitionModel())))) {
                                    if (state.id != null) {
                                        taskViewModel.editTask(context = context)
                                        onDismissRequest()
                                    } else {
                                        taskViewModel.createTask(context = context)
                                        onDismissRequest()
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
                        }) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_paper_airplane),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCustomiseButton(
    title: String,
    imageVector: ImageVector,
    contentColor: Color = MaterialTheme.colorScheme.outline,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f),
                shape = RoundedCornerShape(6.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = contentColor
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = contentColor
            )
        }
    }
}

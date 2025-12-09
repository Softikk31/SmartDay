package com.example.smartday.ui.ui.components.dialogs

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionCustom
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.core.models.task.repetition.values.DayOfWeekRepetition
import com.example.smartday.core.models.task.repetition.values.DaysOfWeekRepetition
import com.example.smartday.core.models.task.repetition.values.MonthDayRepetition
import com.example.smartday.core.models.task.repetition.values.ValueDateRepetition
import com.example.smartday.core.models.task.repetition.values.YearDateRepetition
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.models.enums.TaskFormDialogMode
import com.example.smartday.ui.models.states.TaskFormState
import com.example.smartday.ui.ui.components.Day
import com.example.smartday.ui.ui.components.DaysOfWeekTitle
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.RedPrimary
import com.example.smartday.ui.ui.theme.SmartDayTheme
import com.example.smartday.ui.ui.theme.White
import com.example.smartday.ui.ui.theme.Yellow
import com.example.smartday.ui.utils.rememberFirstMostVisibleMonth
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormDialog(state: TaskFormState, taskViewModel: TaskViewModel, locale: Locale) {
    val daysOfWeek: List<DayOfWeek> = DayOfWeek.entries
    val timeByTimePickerState = state.time ?: LocalTime.now()
    val dialogMode by taskViewModel.taskFormDialogMode.collectAsState()

    val selectedDate =
        rememberSaveable(dialogMode) { mutableStateOf(state.date ?: LocalDate.now()) }

    val selectedPriority = rememberSaveable(dialogMode) { mutableStateOf(state.priority) }

    val selectRepetition = key(state.repetition) {
        rememberSaveable(
            stateSaver = Saver(
                save = { repetition -> Json.encodeToString(repetition) },
                restore = { value -> Json.decodeFromString(value) })
        ) {
            mutableStateOf(state.repetition)
        }
    }


    val timePickerState = key(dialogMode) {
        rememberTimePickerState(
            initialHour = timeByTimePickerState.hour,
            initialMinute = timeByTimePickerState.minute,
            is24Hour = true,
        )
    }

    val currentMonth: YearMonth = remember(dialogMode) { state.date?.yearMonth ?: YearMonth.now() }
    val startMonth: YearMonth = remember { currentMonth.minusMonths(100) }
    val endMonth: YearMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek: DayOfWeek = remember { firstDayOfWeekFromLocale() }

    val stateCalendar: CalendarState = key(dialogMode) {
        rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = firstDayOfWeek
        )
    }

    val firstVisibleMonth: CalendarMonth =
        rememberFirstMostVisibleMonth(stateCalendar, viewportPercent = 50f)

    val showTimeDialog = remember { mutableStateOf(true) }

    val imageVector =
        if (showTimeDialog.value) ImageVector.vectorResource(R.drawable.ic_keyboard)
        else ImageVector.vectorResource(R.drawable.ic_clock)


    var expanded by remember { mutableStateOf(false) }
    val animationRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, animationSpec = tween(200)
    )

    val selectDaysListRepetitionTypeCustom = rememberSaveable(dialogMode) {
        mutableStateListOf<DayOfWeek>().apply {
            val repetition = state.repetition.value
            if (repetition is DaysOfWeekRepetition) {
                repetition.value.map {
                    add(
                        it
                    )
                }
            }
        }
    }

    LaunchedEffect(dialogMode) {
        if (selectDaysListRepetitionTypeCustom.isEmpty()) {
            selectDaysListRepetitionTypeCustom.add(LocalDate.now().dayOfWeek)
        }
    }

    val selectTypeRepetitionTypeCustom = rememberSaveable(dialogMode) {
        mutableStateOf(
            if (state.repetition.type is TaskTypeRepetition.OnCustomTypeRepetition) {
                (state.repetition.type as TaskTypeRepetition.OnCustomTypeRepetition).enumClass
            } else {
                TaskTypeRepetitionCustom.DAY
            }
        )
    }

    val counterTextFieldStateRepetitionTypeCustom = key(dialogMode) {
        rememberTextFieldState(
            state.repetition.counter.toString()
        )
    }


    SmartDayTheme {
        when (dialogMode) {
            TaskFormDialogMode.DATE_DIALOG_MODE -> {
                BackHandler {
                    taskViewModel.nonDialogMode()
                }

                CustomDialog(
                    title = stringResource(R.string.task_create_button_title_date),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    onSelected = {
                        if (selectedDate.value != null) {
                            taskViewModel.onDateSelected(selectedDate.value)
                        } else {
                            taskViewModel.onDateSelected(null)
                            taskViewModel.onTimeSelected(null)
                            taskViewModel.onSelectRepetition(TaskRepetitionModel())
                        }
                        taskViewModel.nonDialogMode()
                    },
                    onDismiss = {
                        selectedDate.value = state.date
                        taskViewModel.nonDialogMode()
                    }) {

                    ButtonArrow(
                        modifier = Modifier
                            .height(46.dp)
                            .padding(horizontal = 16.dp),
                        title = stringResource(R.string.task_category_no_date)
                    ) {
                        taskViewModel.onDateSelected(null)
                        taskViewModel.nonDialogMode()
                    }

                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                    )

                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "${
                            firstVisibleMonth.yearMonth.month.getDisplayName(
                                TextStyle.FULL_STANDALONE, locale
                            ).replaceFirstChar { it.uppercase() }
                        }, ${firstVisibleMonth.yearMonth.year}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface)

                    DaysOfWeekTitle(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        daysOfWeek = daysOfWeek,
                        locale = locale
                    )
                    HorizontalCalendar(
                        modifier = Modifier.animateContentSize(
                            animationSpec = tween(
                                durationMillis = 350, easing = FastOutSlowInEasing
                            )
                        ), state = stateCalendar, dayContent = { day ->
                            Day(
                                day = day,
                                isSelected = if (selectedDate.value != null) selectedDate.value == day.date else false,
                                onClick = {
                                    selectedDate.value = it.date
                                })
                        })
                }
            }

            TaskFormDialogMode.TIME_DIALOG_MODE -> {
                BackHandler {
                    taskViewModel.nonDialogMode()
                }

                CustomDialogTimePicker(
                    title = stringResource(R.string.notification_time),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    imageVector = imageVector,
                    onClickIcon = {
                        showTimeDialog.value = !showTimeDialog.value
                    },
                    onSelected = {
                        taskViewModel.onTimeSelected(
                            LocalTime.of(
                                timePickerState.hour, timePickerState.minute
                            )
                        )
                        taskViewModel.nonDialogMode()

                    },
                    onDismiss = {
                        taskViewModel.nonDialogMode()
                    }) {
                    ButtonArrow(
                        modifier = Modifier
                            .height(46.dp)
                            .padding(horizontal = 16.dp),
                        title = stringResource(R.string.task_category_no_time)
                    ) {
                        taskViewModel.onTimeSelected(null)
                        taskViewModel.nonDialogMode()
                    }

                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                    )

                    if (showTimeDialog.value) {
                        TimePicker(
                            state = timePickerState, colors = TimePickerDefaults.colors(
                                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                                    0.6f
                                ),
                                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
                                clockDialSelectedContentColor = MaterialTheme.colorScheme.surface,
                                clockDialColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    } else {
                        TimeInput(
                            state = timePickerState, colors = TimePickerDefaults.colors(
                                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                                    0.6f
                                ),
                                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            TaskFormDialogMode.PRIORITY_DIALOG_MODE -> {
                CustomDialog(
                    title = stringResource(R.string.task_create_button_title_priority),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    onSelected = {
                        taskViewModel.onSelectPriority(selectedPriority.value)
                        taskViewModel.nonDialogMode()
                    },
                    onDismiss = {
                        taskViewModel.nonDialogMode()
                    }) {
                    Column {
                        ButtonRadio(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            selected = selectedPriority.value == TaskPriority.NULL,
                            icon = ImageVector.vectorResource(R.drawable.ic_flag),
                            iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            title = stringResource(R.string.none),
                            onClick = { selectedPriority.value = TaskPriority.NULL })
                        ButtonRadio(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            selected = selectedPriority.value == TaskPriority.LOW,
                            icon = ImageVector.vectorResource(R.drawable.ic_flag),
                            iconColor = Blue,
                            title = stringResource(R.string.low_priority),
                            onClick = { selectedPriority.value = TaskPriority.LOW })
                        ButtonRadio(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            selected = selectedPriority.value == TaskPriority.MEDIUM,
                            icon = ImageVector.vectorResource(R.drawable.ic_flag),
                            iconColor = Yellow,
                            title = stringResource(R.string.medium_priority),
                            onClick = { selectedPriority.value = TaskPriority.MEDIUM })
                        ButtonRadio(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            selected = selectedPriority.value == TaskPriority.HIGH,
                            icon = ImageVector.vectorResource(R.drawable.ic_flag),
                            iconColor = RedPrimary,
                            title = stringResource(R.string.high_priority),
                            onClick = { selectedPriority.value = TaskPriority.HIGH })
                    }
                }
            }

            TaskFormDialogMode.SELECT_REPETITION_DIALOG_MODE -> {
                CustomDialog(
                    title = stringResource(R.string.task_create_button_title_repetition),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    onSelected = {
                        taskViewModel.onSelectRepetition(selectRepetition.value)
                        taskViewModel.nonDialogMode()
                    },
                    onDismiss = {
                        taskViewModel.nonDialogMode()
                    }) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.NULL
                            ),
                            title = stringResource(R.string.repeat_never)
                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = null,
                                type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.NULL
                                )
                            )
                        }

                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.EVERY_DAY
                            ),
                            title = stringResource(R.string.repeat_every_day)
                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = DayOfWeekRepetition(
                                    LocalDate.now().dayOfWeek
                                ),
                                type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.EVERY_DAY
                                )
                            )

                        }
                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.EVERY_WEEK_ON
                            ),
                            title = stringResource(
                                R.string.repeat_every_week_on,
                                state.date?.dayOfWeek?.getDisplayName(
                                    TextStyle.SHORT_STANDALONE, locale
                                ) ?: LocalDate.now().dayOfWeek.getDisplayName(
                                    TextStyle.SHORT_STANDALONE, locale
                                )
                            )
                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = DayOfWeekRepetition(
                                    state.date?.dayOfWeek ?: LocalDate.now().dayOfWeek
                                ), type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.EVERY_WEEK_ON
                                )
                            )

                        }
                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.EVERY_WORKDAY
                            ),
                            title = stringResource(R.string.repeat_every_workday)
                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = DaysOfWeekRepetition(
                                    DayOfWeek.entries.filter {
                                        it !in listOf(
                                            DayOfWeek.SUNDAY, DayOfWeek.SATURDAY
                                        )
                                    }), type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.EVERY_WORKDAY
                                )
                            )

                        }
                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.EVERY_MONTH_DATE
                            ),
                            title = stringResource(
                                R.string.repeat_every_month_on,
                                state.date?.dayOfMonth ?: LocalDate.now().dayOfMonth
                            )

                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = MonthDayRepetition(
                                    state.date?.dayOfMonth ?: LocalDate.now().dayOfMonth
                                ), type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.EVERY_MONTH_DATE
                                )
                            )

                        }

                        val displayDate = state.date?.let {
                            val month = it.month.getDisplayName(TextStyle.SHORT_STANDALONE, locale)
                            val day = it.dayOfMonth
                            "$day $month"
                        } ?: run {
                            val now = LocalDate.now()
                            val month = now.month.getDisplayName(TextStyle.SHORT_STANDALONE, locale)
                            val day = now.dayOfMonth
                            "$day $month"
                        }

                        ButtonRadio(
                            modifier = Modifier
                                .height(46.dp)
                                .padding(horizontal = 16.dp),
                            selected = selectRepetition.value.type == TaskTypeRepetition.OnSystemTypeRepetition(
                                TaskTypeRepetitionSystem.EVERY_YEAR_DATE
                            ),
                            title = stringResource(
                                R.string.repeat_every_year_on, displayDate
                            )
                        ) {
                            selectRepetition.value = TaskRepetitionModel(
                                value = YearDateRepetition(
                                    ValueDateRepetition(
                                        month = state.date?.month?.value
                                            ?: LocalDate.now().month.value,
                                        dayOfMonth = state.date?.dayOfMonth
                                            ?: LocalDate.now().dayOfMonth
                                    )
                                ), type = TaskTypeRepetition.OnSystemTypeRepetition(
                                    TaskTypeRepetitionSystem.EVERY_YEAR_DATE
                                )
                            )
                        }

                        Column {
                            Spacer(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                            )
                            ButtonRadio(
                                modifier = Modifier
                                    .height(46.dp)
                                    .padding(horizontal = 16.dp),
                                selected = selectRepetition.value.type == TaskTypeRepetition.OnCustomTypeRepetition(
                                    selectTypeRepetitionTypeCustom.value
                                ),
                                title = stringResource(
                                    R.string.custom_select_repetition
                                )
                            ) {
                                taskViewModel.setupRepetitionSelection()
                            }
                            Spacer(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                            )
                        }
                    }
                }
            }

            TaskFormDialogMode.SETUP_REPETITION_DIALOG_MODE -> {
                CustomDialog(
                    title = stringResource(R.string.task_create_button_title_repetition),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    onSelected = {
                        if (counterTextFieldStateRepetitionTypeCustom.text.isNotEmpty()) {
                            taskViewModel.onSelectRepetition(
                                TaskRepetitionModel(
                                    value = when (selectTypeRepetitionTypeCustom.value) {
                                        TaskTypeRepetitionCustom.DAY -> DayOfWeekRepetition(
                                            LocalDate.now().dayOfWeek
                                        )

                                        TaskTypeRepetitionCustom.WEEK -> DaysOfWeekRepetition(
                                            selectDaysListRepetitionTypeCustom
                                        )

                                        TaskTypeRepetitionCustom.MONTH -> MonthDayRepetition(
                                            state.date?.dayOfMonth ?: LocalDate.now().dayOfMonth
                                        )

                                        TaskTypeRepetitionCustom.YEAR -> YearDateRepetition(
                                            ValueDateRepetition(
                                                month = state.date?.month?.value
                                                    ?: LocalDate.now().month.value,
                                                dayOfMonth = state.date?.dayOfMonth
                                                    ?: LocalDate.now().dayOfMonth
                                            )
                                        )

                                    },
                                    type = TaskTypeRepetition.OnCustomTypeRepetition(
                                        selectTypeRepetitionTypeCustom.value
                                    ),
                                    counter = counterTextFieldStateRepetitionTypeCustom.text.toString()
                                        .toInt()
                                )
                            )

                            taskViewModel.nonDialogMode()
                        }
                    },
                    onDismiss = {
                        taskViewModel.repetitionSelection()
                    }) {
                    LaunchedEffect(counterTextFieldStateRepetitionTypeCustom.text) {
                        counterTextFieldStateRepetitionTypeCustom.edit {
                            val current = counterTextFieldStateRepetitionTypeCustom.text

                            var text = current.filter { it.isDigit() }

                            if (text.isNotEmpty()) {
                                if (text[0] == '0') {
                                    text = text.drop(1)
                                }
                            }

                            if (text.length > 3) {
                                text = text.take(3)
                            }

                            if (current != text) {
                                replace(0, current.length, text)
                            }

                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = stringResource(R.string.every),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                enabled = if (counterTextFieldStateRepetitionTypeCustom.text.toString()
                                        .isNotEmpty()
                                ) {
                                    counterTextFieldStateRepetitionTypeCustom.text.toString()
                                        .toInt() > 1
                                } else false, onClick = {
                                    counterTextFieldStateRepetitionTypeCustom.edit {
                                        val count =
                                            counterTextFieldStateRepetitionTypeCustom.text.toString()
                                                .toInt() - 1
                                        replace(
                                            0,
                                            counterTextFieldStateRepetitionTypeCustom.text.length,
                                            count.toString()
                                        )
                                    }
                                }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_minus_circle),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            TextField(
                                modifier = Modifier
                                    .height(52.dp)
                                    .width(58.dp),
                                state = counterTextFieldStateRepetitionTypeCustom,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Center
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone
                                ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        0.1f
                                    ),
                                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        0.1f
                                    ),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    selectionColors = TextSelectionColors(
                                        handleColor = MaterialTheme.colorScheme.primary,
                                        backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                            0.4f
                                        )
                                    )
                                ),
                                contentPadding = PaddingValues(0.dp),
                                shape = RoundedCornerShape(4.dp),
                                lineLimits = TextFieldLineLimits.SingleLine
                            )
                            IconButton(
                                enabled = if (counterTextFieldStateRepetitionTypeCustom.text.toString()
                                        .isNotEmpty()
                                ) {
                                    counterTextFieldStateRepetitionTypeCustom.text.toString()
                                        .toInt() < 999
                                } else true, onClick = {
                                    counterTextFieldStateRepetitionTypeCustom.edit {
                                        if (counterTextFieldStateRepetitionTypeCustom.text.toString()
                                                .isNotEmpty()
                                        ) {
                                            val count =
                                                counterTextFieldStateRepetitionTypeCustom.text.toString()
                                                    .toInt() + 1
                                            replace(
                                                0,
                                                counterTextFieldStateRepetitionTypeCustom.text.length,
                                                count.toString()
                                            )
                                        } else {
                                            replace(
                                                0,
                                                counterTextFieldStateRepetitionTypeCustom.text.length,
                                                2.toString()
                                            )
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_plus_circle),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(120.dp)
                                    .height(58.dp)
                                    .padding(end = 16.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            0.1f
                                        ), RoundedCornerShape(4.dp)
                                    )
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        expanded = !expanded
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(start = 12.dp),
                                    text = when (selectTypeRepetitionTypeCustom.value) {
                                        TaskTypeRepetitionCustom.DAY -> stringResource(R.string.day)
                                        TaskTypeRepetitionCustom.WEEK -> stringResource(R.string.week)
                                        TaskTypeRepetitionCustom.MONTH -> stringResource(R.string.month)
                                        TaskTypeRepetitionCustom.YEAR -> stringResource(R.string.year)
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Icon(
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .size(18.dp)
                                        .graphicsLayer(rotationX = animationRotation),
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down_incomplete),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ) {
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(R.string.day),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }, onClick = {
                                    selectTypeRepetitionTypeCustom.value =
                                        TaskTypeRepetitionCustom.DAY
                                    expanded = false
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(R.string.week),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }, onClick = {
                                    selectTypeRepetitionTypeCustom.value =
                                        TaskTypeRepetitionCustom.WEEK
                                    expanded = false
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(R.string.month),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }, onClick = {
                                    selectTypeRepetitionTypeCustom.value =
                                        TaskTypeRepetitionCustom.MONTH
                                    expanded = false
                                })
                                DropdownMenuItem(text = {
                                    Text(
                                        stringResource(R.string.year),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }, onClick = {
                                    selectTypeRepetitionTypeCustom.value =
                                        TaskTypeRepetitionCustom.YEAR
                                    expanded = false
                                })
                            }
                        }
                    }


                    if (selectTypeRepetitionTypeCustom.value == TaskTypeRepetitionCustom.WEEK) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = stringResource(R.string.day_of_the_week),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        WeekDaysSelect(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            locale = locale,
                            selectDaysList = selectDaysListRepetitionTypeCustom
                        ) { day ->
                            if (day !in selectDaysListRepetitionTypeCustom) {
                                selectDaysListRepetitionTypeCustom.add(day)
                            } else {
                                if (selectDaysListRepetitionTypeCustom.size >= 2) {
                                    selectDaysListRepetitionTypeCustom.remove(day)
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                        )
                    }
                }
            }

            else -> {

            }
        }
    }
}


@Composable
fun ButtonRadio(
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    title: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            RadioButton(
                selected = selected, onClick = null, colors = colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun WeekDaysSelect(
    modifier: Modifier = Modifier,
    locale: Locale,
    selectDaysList: List<DayOfWeek>,
    onClickDay: (DayOfWeek) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            CalendarWeekDay(
                text = day.getDisplayName(TextStyle.SHORT_STANDALONE, locale),
                containerColor = if (day in selectDaysList) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = if (day in selectDaysList) White
                else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                onClickDay(day)
            }
        }
    }
}


@Composable
fun CalendarWeekDay(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .background(
                color = containerColor, shape = CircleShape
            )
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = MaterialTheme.typography.bodyLarge, color = contentColor
        )
    }
}

@Composable
fun ButtonRadio(
    selected: Boolean, modifier: Modifier = Modifier, title: String, onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            RadioButton(
                selected = selected, onClick = null, colors = colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun ButtonArrow(
    modifier: Modifier = Modifier, title: String, onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.clickable(
            onClick = onClick
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                modifier = Modifier.size(23.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right_incomplete),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
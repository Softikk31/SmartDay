package com.example.smartday.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.presentation.main.DateTimeDialogMode
import com.example.smartday.presentation.main.TaskViewModel
import com.example.smartday.presentation.models.states.TaskState
import com.example.smartday.presentation.ui.screens.ButtonCreateTask
import com.example.smartday.presentation.ui.theme.SmartDayTheme
import com.example.smartday.presentation.utils.rememberFirstMostVisibleMonth
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeDialog(state: TaskState, taskViewModel: TaskViewModel, locale: Locale) {
    val daysOfWeek: List<DayOfWeek> = DayOfWeek.entries

    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    val selectedDate: MutableState<LocalDate?> = rememberSaveable { mutableStateOf(null) }
    val selectedTime = rememberSaveable(
        timePickerState.hour,
        timePickerState.minute
    ) { mutableStateOf(LocalTime.of(timePickerState.hour, timePickerState.minute)) }

    val currentMonth: YearMonth = remember { YearMonth.now() }
    val startMonth: YearMonth = remember { currentMonth.minusMonths(100) }
    val endMonth: YearMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek: DayOfWeek = remember { firstDayOfWeekFromLocale() }

    val stateCalendar: CalendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val firstVisibleMonth: CalendarMonth = rememberFirstMostVisibleMonth(stateCalendar, viewportPercent = 50f)

    LaunchedEffect(Unit) {
        stateCalendar.scrollToMonth(currentMonth)
    }

    val dateTimeMode by taskViewModel.dateTimeMode.collectAsState()

    val showDial = remember { mutableStateOf(true) }

    val imageVector = if (showDial.value) ImageVector.vectorResource(R.drawable.ic_input_time_picker)
    else ImageVector.vectorResource(R.drawable.ic_clock)

    SmartDayTheme {
        when (dateTimeMode) {
            DateTimeDialogMode.DATE_MODE -> {
                BackHandler {
                    taskViewModel.onDismissDeleteAndEditTask()
                    taskViewModel.nonDateTimeSelection()
                }

                CustomDialog(
                    title = "${
                        firstVisibleMonth.yearMonth.month.getDisplayName(
                            TextStyle.FULL_STANDALONE,
                            locale
                        ).replaceFirstChar { it.uppercase() }
                    }, ${firstVisibleMonth.yearMonth.year}",
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    onSelected = {
                        taskViewModel.onDateSelected(selectedDate.value)
                        taskViewModel.nonDateTimeSelection()
                    },
                    onDismiss = {
                        taskViewModel.onDismissDeleteAndEditTask()
                        taskViewModel.nonDateTimeSelection()
                    }
                ) {
                    DaysOfWeekTitle(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        daysOfWeek = daysOfWeek,
                        locale = locale
                    )

                    HorizontalCalendar(
                        state = stateCalendar, dayContent = { day ->
                            if ((selectedDate.value == null) and (day.date == LocalDate.now())) {
                                selectedDate.value = day.date
                            }
                            Day(
                                day = day,
                                isSelected = selectedDate.value == day.date,
                                onClick = { selectedDate.value = it.date })
                        }
                    )

                    Column {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                        )
                        ButtonCreateTask(
                            icon = ImageVector.vectorResource(R.drawable.ic_bell),
                            title = stringResource(R.string.time),
                            value = state.time?.toString() ?: stringResource(R.string.no_time)
                        ) {
                            taskViewModel.timeSelection()
                        }
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.1f))
                        )
                    }
                }
            }

            DateTimeDialogMode.TIME_MODE -> {
                BackHandler {
                    if (state.date == null) {
                        taskViewModel.onTimeDismiss()
                        taskViewModel.dateSelection()
                    } else {
                        taskViewModel.onTimeDismiss()
                        taskViewModel.nonDateTimeSelection()
                    }
                }

                CustomDialogTimePicker(
                    title = stringResource(R.string.notification_time),
                    titleStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    imageVector = imageVector,
                    onSelected = {
                        if (state.date == null) {
                            taskViewModel.onTimeSelected(selectedTime.value)
                            taskViewModel.dateSelection()
                        } else {
                            taskViewModel.onTimeSelected(selectedTime.value)
                            taskViewModel.nonDateTimeSelection()
                        }
                    },
                    onClickIcon = {
                        showDial.value = !showDial.value
                    },
                    onDismiss = {
                        if (state.date == null) {
                            taskViewModel.onTimeDismiss()
                            taskViewModel.dateSelection()
                        } else {
                            taskViewModel.onTimeDismiss()
                            taskViewModel.nonDateTimeSelection()
                        }
                    }
                ) {
                    if (showDial.value) {
                        TimePicker(
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.6f),
                                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
                                clockDialColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    } else {
                        TimeInput(
                            state = timePickerState,
                            colors = TimePickerDefaults.colors(
                                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.6f),
                                timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            else -> {

            }
        }
    }
}

package com.example.smartday.ui.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.components.*
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.utils.rememberFirstMostVisibleMonth
import com.example.smartday.ui.utils.rememberFirstVisibleWeek
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.FlowPreview
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, FlowPreview::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, taskViewModel: TaskViewModel) {
    val daysOfWeek: List<DayOfWeek> = DayOfWeek.entries
    val locale: Locale = when (Locale.getDefault().language) {
        "ru" -> Locale.forLanguageTag("ru")
        else -> Locale.ENGLISH
    }

    var isWeekMode by remember { mutableStateOf(false) }

    val selectedDate: MutableState<LocalDate?> = rememberSaveable { mutableStateOf(null) }
    val tasksByDate by viewModel.dateTasks.collectAsState()

    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember(currentDate) { currentDate.yearMonth }
    val startMonth = remember(currentDate) { currentMonth.minusMonths(100) }
    val endMonth = remember(currentDate) { currentMonth.plusMonths(100) }
    val firstDayOfWeek: DayOfWeek = remember { firstDayOfWeekFromLocale() }

    LaunchedEffect(Unit) {
        selectedDate.value = currentDate
    }


    val monthState = rememberCalendarState(
        startMonth = startMonth, endMonth = endMonth, firstVisibleMonth = currentMonth, firstDayOfWeek = firstDayOfWeek
    )
    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = firstDayOfWeek
    )

    LaunchedEffect(isWeekMode) {
        if (isWeekMode) {
            val targetDate = selectedDate.value ?: currentDate
            val weeksInMonth = monthState.firstVisibleMonth.weekDays

            val matchingWeek = weeksInMonth.find { week ->
                targetDate in week.map { it.date }
            }

            if (matchingWeek != null) {
                weekState.scrollToWeek(targetDate)
            } else {
                weekState.scrollToWeek(monthState.firstVisibleMonth.weekDays.first().first().date)
            }

        } else {
            val targetMonth = monthState.firstVisibleMonth.yearMonth
            monthState.scrollToMonth(targetMonth)
        }
    }

    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                text = calendarTitle(
                    isWeekMode = isWeekMode, monthState = monthState, weekState = weekState, locale = locale
                ), secondaryText = calendarSecondaryText(
                    isWeekMode = isWeekMode, monthState = monthState, weekState = weekState
                )
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onVerticalDrag = { _, offset ->
                            if (offset < -10) {
                                isWeekMode = true
                            } else if (offset > 10) {
                                isWeekMode = false
                            }
                        })
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            DaysOfWeekTitle(modifier = Modifier.padding(horizontal = 16.dp), daysOfWeek = daysOfWeek, locale = locale)

            AnimatedContent(
                isWeekMode, transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            ),
                            initialOffsetY = { it / 3 }
                        ) + fadeIn(animationSpec = tween(200)) togetherWith
                                slideOutVertically(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    ),
                                    targetOffsetY = { -it / 3 }
                                ) + fadeOut(animationSpec = tween(150))
                    } else {
                        slideInVertically(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = FastOutSlowInEasing
                            ),
                            initialOffsetY = { -it / 3 }
                        ) + fadeIn(animationSpec = tween(200)) togetherWith
                                slideOutVertically(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    ),
                                    targetOffsetY = { it / 3 }
                                ) + fadeOut(animationSpec = tween(150))
                    }
                }
            ) { state ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 350,
                                easing = FastOutSlowInEasing
                            )
                        )
                ) {
                    if (!state) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            HorizontalCalendar(
                                state = monthState, dayContent = { day ->
                                    Day(
                                        day = day,
                                        isSelected = selectedDate.value == day.date,
                                        onClick = { selectedDate.value = it.date })
                                })
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            WeekCalendar(
                                state = weekState, dayContent = { day ->
                                    Day(
                                        day = day,
                                        isSelected = selectedDate.value == day.date,
                                        onClick = { selectedDate.value = it.date })
                                })
                        }
                    }
                }
            }

            AnimatedContent(
                isWeekMode
            ) { state ->
                if (state) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                isWeekMode = false
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(25.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_down),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                isWeekMode = true
                            }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_line),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }


            SelectedDateText(date = selectedDate.value, locale = locale)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tasksByDate.forEach { (date, tasksList) ->
                    if (selectedDate.value == date) {
                        items(tasksList) { task ->
                            key(task.id) {
                                TaskCard(
                                    task = task, taskViewModel = taskViewModel
                                )
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

@Composable
private fun calendarTitle(
    isWeekMode: Boolean, monthState: CalendarState, weekState: WeekCalendarState, locale: Locale
): String {

    val firstVisibleMonth: CalendarMonth = rememberFirstMostVisibleMonth(monthState, viewportPercent = 50f)
    val firstVisibleWeek: Week = rememberFirstVisibleWeek(weekState, viewportPercent = 50f)

    return if (!isWeekMode) firstVisibleMonth.yearMonth.month.getDisplayName(
        TextStyle.FULL_STANDALONE, locale
    ).replaceFirstChar {
        it.uppercase(locale)
    }
    else firstVisibleWeek.days.first().date.yearMonth.month.getDisplayName(
        TextStyle.FULL_STANDALONE, locale
    ).replaceFirstChar {
        it.uppercase(locale)
    }
}


@Composable
private fun calendarSecondaryText(
    isWeekMode: Boolean, monthState: CalendarState, weekState: WeekCalendarState
): String {

    val firstVisibleMonth: CalendarMonth = rememberFirstMostVisibleMonth(monthState, viewportPercent = 50f)
    val firstVisibleWeek: Week = rememberFirstVisibleWeek(weekState, viewportPercent = 50f)

    return if (!isWeekMode) "${firstVisibleMonth.yearMonth.year}, ${firstVisibleMonth.yearMonth.month.value} ${
        stringResource(R.string.secondary_title_month)
    }" else "${firstVisibleWeek.days.first().date.yearMonth.year}, ${firstVisibleWeek.days.first().date.yearMonth.month.value} ${
        stringResource(R.string.secondary_title_month)
    }"
}
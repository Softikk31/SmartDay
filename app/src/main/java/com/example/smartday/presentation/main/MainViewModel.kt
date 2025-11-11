package com.example.smartday.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartday.R
import com.example.smartday.domain.usecase.GetCompletedTasksUseCase
import com.example.smartday.domain.usecase.GetFoundTasksUseCase
import com.example.smartday.domain.usecase.GetTasksByDateUseCase
import com.example.smartday.domain.usecase.GetTasksWithoutDateUseCase
import com.example.smartday.domain.usecase.OverdueTaskUseCase
import com.example.smartday.presentation.mappers.toUI
import com.example.smartday.presentation.models.TaskUI
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class TaskListItem {
    sealed class Title() : TaskListItem() {
        data class IntTitle(val title: Int) : TaskListItem()
        data class DateTitle(val title: LocalDate) : TaskListItem()
    }

    data class TaskList(val taskList: List<TaskUI>) : TaskListItem()
}

class MainViewModel(
    private val getTasksByDateUseCase: GetTasksByDateUseCase,
    private val getCompletedTasksUseCase: GetCompletedTasksUseCase,
    private val getTasksWithoutDateUseCase: GetTasksWithoutDateUseCase,
    private val overdueTaskUseCase: OverdueTaskUseCase,
    context: Context
) : ViewModel() {
    private val _overdueTasks = MutableStateFlow<List<TaskUI>>(emptyList())
    val overdueTasks = _overdueTasks.asStateFlow()

    private val _dateTasks = MutableStateFlow<Map<LocalDate, List<TaskUI>>>(emptyMap())
    val dateTasks: StateFlow<Map<LocalDate, List<TaskUI>>> = _dateTasks.asStateFlow()

    private val _withoutDateTasks = MutableStateFlow<List<TaskUI>>(emptyList())
    val withoutDateTasks: StateFlow<List<TaskUI>> = _withoutDateTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow<List<TaskUI>>(emptyList())
    val completedTasks: StateFlow<List<TaskUI>> = _completedTasks.asStateFlow()

    val allTasksListItem: StateFlow<List<TaskListItem>> = combine(
        overdueTasks, dateTasks, withoutDateTasks, completedTasks
    ) { tasksOverdue, tasksDate, tasksWithout, tasksCompleted ->
        buildList {
            if (tasksOverdue.isNotEmpty()) {
                add(TaskListItem.Title.IntTitle(title = R.string.task_category_overdue))
                add(TaskListItem.TaskList(tasksOverdue))
            }

            tasksDate.forEach { (date, taskList) ->
                add(TaskListItem.Title.DateTitle(date))
                add(TaskListItem.TaskList(taskList))
            }

            if (tasksWithout.isNotEmpty()) {
                add(TaskListItem.Title.IntTitle(title = R.string.task_category_no_date))
                add(TaskListItem.TaskList(tasksWithout))
            }

            if (tasksCompleted.isNotEmpty()) {
                add(TaskListItem.Title.IntTitle(title = R.string.task_category_completed))
                add(TaskListItem.TaskList(tasksCompleted))
            }
        }

    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            getTasksByDateUseCase()
                .map { taskListDomain -> taskListDomain.map { it.toUI() } }
                .distinctUntilChanged()
                .collect { taskListUI ->
                    taskListUI.forEach { task ->
                        scheduleTaskAlarm(context, overdueTaskUseCase, task)
                    }
                    val tasksListOverdue = taskListUI.filter { it.isOverdue }
                    val tasksListDate = taskListUI.filterNot { it.isOverdue }

                    val sortedListTaskDate = tasksListDate
                        .groupBy { it.date!! }
                        .toSortedMap()
                        .mapValues { (_, tasks) ->
                            tasks.sortedWith(
                                compareBy<TaskUI> { it.notification == null }
                                    .thenBy { it.notification }
                            )
                        }

                    val sortedListTaskOverdue = tasksListOverdue
                        .sortedWith(
                            compareBy<TaskUI> { it.date == null }
                                .thenBy { it.date }
                                .thenBy { it.notification == null }
                                .thenBy { it.notification }
                        )

                    _dateTasks.value = sortedListTaskDate
                    _overdueTasks.value = sortedListTaskOverdue
                }
        }

        viewModelScope.launch {
            getTasksWithoutDateUseCase()
                .map { taskListDomain -> taskListDomain.map { it.toUI() } }
                .distinctUntilChanged()
                .collect { _withoutDateTasks.value = it }
        }

        viewModelScope.launch {
            getCompletedTasksUseCase()
                .map { taskListDomain -> taskListDomain.map { it.toUI() } }
                .distinctUntilChanged()
                .collect { taskListUI ->
                    val sortedListTaskCompleted = taskListUI
                        .sortedWith(
                            compareBy<TaskUI> { it.date == null }
                                .thenBy { it.date }
                                .thenBy { it.notification == null }
                                .thenBy { it.notification }
                        )

                    _completedTasks.value = sortedListTaskCompleted
                }
        }
    }
}

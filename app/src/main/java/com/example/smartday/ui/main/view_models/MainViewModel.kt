package com.example.smartday.ui.main.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartday.R
import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.domain.usecase.task.GetCompletedTasksUseCase
import com.example.smartday.domain.usecase.task.GetTasksByDateUseCase
import com.example.smartday.domain.usecase.task.GetTasksWithoutDateUseCase
import com.example.smartday.domain.usecase.task.OverdueTaskUseCase
import com.example.smartday.domain.usecase.task.UpdateTaskUseCase
import com.example.smartday.ui.main.scheduler.scheduleTaskAlarm
import com.example.smartday.ui.models.states.TaskListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(
    private val getTasksByDateUseCase: GetTasksByDateUseCase,
    private val getCompletedTasksUseCase: GetCompletedTasksUseCase,
    private val getTasksWithoutDateUseCase: GetTasksWithoutDateUseCase,
    private val overdueTaskUseCase: OverdueTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    context: Context
) : ViewModel() {
    private val _overdueTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val overdueTasks = _overdueTasks.asStateFlow()

    private val _dateTasks = MutableStateFlow<Map<LocalDate, List<TaskModel>>>(emptyMap())
    val dateTasks: StateFlow<Map<LocalDate, List<TaskModel>>> = _dateTasks.asStateFlow()

    private val _withoutDateTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val withoutDateTasks: StateFlow<List<TaskModel>> = _withoutDateTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val completedTasks: StateFlow<List<TaskModel>> = _completedTasks.asStateFlow()

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
                .distinctUntilChanged()
                .collect { taskList ->
                    taskList.forEach { task ->
                        scheduleTaskAlarm(context, overdueTaskUseCase, updateTaskUseCase, task)
                    }
                    val tasksListOverdue = taskList.filter { it.isOverdue }
                    val tasksListDate = taskList.filterNot { it.isOverdue }

                    val sortedListTaskDate = tasksListDate
                        .groupBy { it.date!! }
                        .mapValues { (_, tasks) ->
                            tasks.sortedWith(
                                compareByDescending<TaskModel> { it.priority }
                                    .thenBy { it.date }
                                    .thenBy { it.notification }
                                    .thenBy { it.notification == null }
                            )
                        }

                    val sortedListTaskOverdue = tasksListOverdue
                        .sortedWith(
                            compareByDescending<TaskModel> { it.priority }
                                .thenBy { it.date }
                                .thenBy { it.notification }
                                .thenBy { it.notification == null }
                        )

                    _dateTasks.value = sortedListTaskDate
                    _overdueTasks.value = sortedListTaskOverdue
                }
        }

        viewModelScope.launch {
            getTasksWithoutDateUseCase()
                .distinctUntilChanged()
                .collect { taskList ->
                    val sortedListTaskPriority = taskList
                        .sortedWith(
                            compareBy { it.priority }
                        )
                    _withoutDateTasks.value = sortedListTaskPriority
                }
        }

        viewModelScope.launch {
            getCompletedTasksUseCase()
                .distinctUntilChanged()
                .collect { taskList ->
                    val sortedListTaskCompleted = taskList
                        .sortedWith(
                            compareByDescending<TaskModel> { it.priority }
                                .thenBy { it.date }
                                .thenBy { it.notification }
                                .thenBy { it.notification == null }
                        )

                    _completedTasks.value = sortedListTaskCompleted
                }
        }
    }
}

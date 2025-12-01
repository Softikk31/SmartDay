package com.example.smartday.ui.main.view_models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.domain.usecase.task.CompletingTaskUseCase
import com.example.smartday.domain.usecase.task.CreateTaskUseCase
import com.example.smartday.domain.usecase.task.DeleteTaskUseCase
import com.example.smartday.domain.usecase.task.GetAllTasksUseCase
import com.example.smartday.domain.usecase.task.GetFoundTasksUseCase
import com.example.smartday.domain.usecase.task.GetTaskUseCase
import com.example.smartday.domain.usecase.task.UpdateTaskUseCase
import com.example.smartday.ui.main.scheduler.cancelTaskAlarm
import com.example.smartday.ui.models.enums.TaskFormDialogMode
import com.example.smartday.ui.models.states.SelectModeState
import com.example.smartday.ui.models.states.TaskErrorState
import com.example.smartday.ui.models.states.TaskFormState
import com.example.smartday.ui.models.states.TaskFoundState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class TaskViewModel(
    private val getFoundTasksUseCase: GetFoundTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completingTaskUseCase: CompletingTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    context: Context
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<TaskModel>>(emptyList())

    private val _selectModeState = MutableStateFlow(SelectModeState())
    val deleteMode = _selectModeState.asStateFlow()

    private val _taskFormDialogMode = MutableStateFlow(TaskFormDialogMode.NON_DIALOG_MODE)
    val taskFormDialogMode = _taskFormDialogMode.asStateFlow()

    private val _stateTaskForm = MutableStateFlow(TaskFormState())
    val stateTaskForm = _stateTaskForm.asStateFlow()

    private val _error = MutableStateFlow(TaskErrorState())


    init {
        viewModelScope.launch {
            _error.collect {
                _error.value.message?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModelScope.launch {
            getAllTasksUseCase().distinctUntilChanged().collect { taskList ->
                _allTasks.value = taskList
            }
        }


        viewModelScope.launch {
            _selectModeState.map { it.selectedIds }.distinctUntilChanged().collect {
                uncheckSelectAllIfAllSelected()
            }
        }

        viewModelScope.launch {
            _allTasks.map { taskListUI -> taskListUI.map { it.id } }.distinctUntilChanged()
                .collect {
                    onDismissDeleteModeByAllTasks()
                }
        }
    }



    fun onSelectRepetition(repetition: TaskRepetitionModel) {
        _stateTaskForm.update { it.copy(repetition = repetition) }
    }

    fun onSelectPriority(priority: TaskPriority) {
        _stateTaskForm.update { it.copy(priority = priority) }
    }

    fun prioritySelection() {
        _taskFormDialogMode.value = TaskFormDialogMode.PRIORITY_DIALOG_MODE
    }

    fun dateSelection() {
        _taskFormDialogMode.value = TaskFormDialogMode.DATE_DIALOG_MODE
    }

    fun timeSelection() {
        _taskFormDialogMode.value = TaskFormDialogMode.TIME_DIALOG_MODE
    }

    fun repetitionSelection() {
        _taskFormDialogMode.value = TaskFormDialogMode.SELECT_REPETITION_DIALOG_MODE
    }

    fun setupRepetitionSelection() {
        _taskFormDialogMode.value = TaskFormDialogMode.SETUP_REPETITION_DIALOG_MODE
    }

    fun nonDialogMode() {
        _taskFormDialogMode.value = TaskFormDialogMode.NON_DIALOG_MODE
    }

    fun checkedSelectAll(checked: Boolean) {
        _selectModeState.update { it.copy(selectAll = checked) }
    }

    fun deleteMode(checked: Boolean) {
        _selectModeState.update { it.copy(isDeleting = checked) }
    }

    fun cleaningSelectedIdsList() {
        _selectModeState.update { it.copy(selectedIds = emptyList()) }
    }

    fun taskInSelectedTaskById(taskId: Long): Boolean {
        return taskId in _selectModeState.value.selectedIds
    }

    fun onDismissDeleteModeByAllTasks() {
        if ((_allTasks.value.isEmpty()) and (_selectModeState.value.isDeleting)) {
            cleaningSelectedIdsList()
            deleteMode(false)
            checkedSelectAll(false)
        }
    }

    fun onDismissDeleteMode() {
        cleaningSelectedIdsList()
        deleteMode(false)
        checkedSelectAll(false)
    }

    fun onIdChange(id: Long) {
        _stateTaskForm.update { it.copy(id = id) }
    }

    fun onTitleChange(title: String) {
        _stateTaskForm.update { it.copy(title = title) }
    }

    fun onDateSelected(date: LocalDate?) {
        _stateTaskForm.update { it.copy(date = date) }
    }

    fun onTimeSelected(time: LocalTime?) {
        _stateTaskForm.update { it.copy(time = time) }
    }

    fun selectTaskId(taskId: Long) {
        val selectedIds = _selectModeState.value.selectedIds
        _selectModeState.update {
            it.copy(
                selectedIds = if (taskId !in selectedIds) selectedIds + taskId
                else selectedIds - taskId
            )
        }
    }

    fun selectAllTaskId() {
        _selectModeState.update { current ->
            if (current.selectAll) {
                val allTasks = _allTasks.value.map { it.id }
                current.copy(selectedIds = allTasks)
            } else {
                current.copy(selectedIds = emptyList())
            }
        }
    }

    fun uncheckSelectAllIfAllSelected() {
        val selectedIds = _selectModeState.value.selectedIds.sorted()
        val allTasksIds = _allTasks.value.map { it.id }.sorted()
        if (selectedIds == allTasksIds) {
            checkedSelectAll(true)
        } else {
            checkedSelectAll(false)
        }
    }

    fun onEditParamSelected(
        id: Long,
        title: String,
        priority: TaskPriority,
        repetition: TaskRepetitionModel,
        date: LocalDate?,
        time: LocalTime?
    ) {
        onIdChange(id = id)
        onTitleChange(title = title)
        onSelectPriority(priority = priority)
        onSelectRepetition(repetition = repetition)
        onDateSelected(date = date)
        onTimeSelected(time = time)
    }

    fun onDismissDeleteAndEditTask() {
        _stateTaskForm.update { TaskFormState() }
    }

    fun getFoundTasks(title: String): Flow<TaskFoundState> {
        if (title.isEmpty()) {
            return flowOf(TaskFoundState())
        }

        return getFoundTasksUseCase(title).map { taskList ->
            val sortedByMatchIndex = taskList.sortedBy { it.title.indexOf(title.lowercase()) }
            val grouped = mapOf(title.lowercase() to sortedByMatchIndex)
            TaskFoundState(matchesTitle = grouped)
        }
    }

    fun createTask(context: Context) {
        viewModelScope.launch {
            val result =
                createTaskUseCase(
                    task = TaskModel(
                        title = _stateTaskForm.value.title.trim(),
                        repetition = _stateTaskForm.value.repetition,
                        priority = TaskPriority.entries.find {
                            it.ordinal == _stateTaskForm.value.priority.ordinal
                        } ?: TaskPriority.NULL,
                        date = _stateTaskForm.value.date,
                        notification = _stateTaskForm.value.time,
                    ))
            result.onFailure { error ->
                when (error.message) {
                    "EMPTY_TITLE" -> {
                        _error.value = TaskErrorState(
                            typeError = error,
                            message = context.getString(R.string.toast_warning_empty_title)
                        )
                    }

                    "NO_DATE_BUT_TIME" -> {
                        _error.value = TaskErrorState(
                            typeError = error,
                            message = context.getString(R.string.toast_warning_null_date_and_not_null_time)
                        )
                    }

                    "NO_DATE_BUT_REPETITION" -> {
                        _error.value = TaskErrorState(
                            typeError = error,
                            message = context.getString(R.string.toast_warning_null_date_and_not_null_repetition)
                        )
                    }
                }

                Log.e(context.getString(R.string.app_name), null, error)
            }
            _stateTaskForm.update { TaskFormState() }
        }
    }

    fun editTask(context: Context) {
        _stateTaskForm.value.id?.let { taskId ->
            viewModelScope.launch {
                val result =
                    updateTaskUseCase(
                        task = TaskModel(
                            id = taskId,
                            title = _stateTaskForm.value.title.trim(),
                            repetition = _stateTaskForm.value.repetition,
                            priority = TaskPriority.entries.find {
                                it.ordinal == _stateTaskForm.value.priority.ordinal
                            } ?: TaskPriority.NULL,
                            date = _stateTaskForm.value.date,
                            notification = _stateTaskForm.value.time,
                        ))
                result.onFailure { error ->
                    when (error.message) {
                        "EMPTY_TITLE" -> {
                            _error.value = TaskErrorState(
                                typeError = error,
                                message = context.getString(R.string.toast_warning_empty_title)
                            )
                        }

                        "NO_DATE_BUT_TIME" -> {
                            _error.value = TaskErrorState(
                                typeError = error,
                                message = context.getString(R.string.toast_warning_null_date_and_not_null_time)
                            )
                        }

                        "NO_DATE_BUT_REPETITION" -> {
                            _error.value = TaskErrorState(
                                typeError = error,
                                message = context.getString(R.string.toast_warning_null_date_and_not_null_repetition)
                            )
                        }
                    }

                    Log.e(context.getString(R.string.app_name), null, error)
                }
                _stateTaskForm.update { TaskFormState() }
            }
        }
    }

    fun deleteTask(context: Context) {
        if (_selectModeState.value.selectedIds.isNotEmpty()) {
            viewModelScope.launch {
                _selectModeState.value.selectedIds.forEach { taskId ->
                    cancelTaskAlarm(context, taskId)
                    deleteTaskUseCase(taskId)
                }
            }
            cleaningSelectedIdsList()
        }
    }

    fun completingTask(taskId: Long, context: Context) {
        viewModelScope.launch {
            if (getTaskUseCase(taskId).repetition == TaskRepetitionModel()) {
                cancelTaskAlarm(context, taskId)
            }
            completingTaskUseCase(taskId = taskId)
        }
    }
}

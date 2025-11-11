package com.example.smartday.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartday.domain.models.TaskDomain
import com.example.smartday.domain.usecase.*
import com.example.smartday.presentation.mappers.toUI
import com.example.smartday.presentation.models.TaskUI
import com.example.smartday.presentation.models.states.TaskState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

enum class DateTimeDialogMode {
    NON_DIALOG_MODE, DATE_MODE, TIME_MODE
}

data class SelectMode(
    val isDeleting: Boolean = false, val selectAll: Boolean = false, val selectedIds: List<Long> = emptyList()
)

data class TaskFoundForm(
    val matchesTitle: Map<String, List<TaskUI>> = emptyMap()
)

class TaskViewModel(
    private val getFoundTasksUseCase: GetFoundTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val completingTaskUseCase: CompletingTaskUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {
    private val _allTasks = MutableStateFlow<List<TaskUI>>(emptyList())

    private val _selectMode = MutableStateFlow(SelectMode())
    val deleteMode = _selectMode.asStateFlow()

    private val _dateTimeMode = MutableStateFlow(DateTimeDialogMode.NON_DIALOG_MODE)
    val dateTimeMode = _dateTimeMode.asStateFlow()

    private val _stateAddAndEditTask = MutableStateFlow(TaskState())
    val stateAddAndEditTask = _stateAddAndEditTask.asStateFlow()

    init {
        viewModelScope.launch {
            getAllTasksUseCase().map { taskListDomain -> taskListDomain.map { it.toUI() } }.distinctUntilChanged()
                .collect { taskListUI ->
                    _allTasks.value = taskListUI
                }
        }


        viewModelScope.launch {
            _selectMode.map { it.selectedIds }.distinctUntilChanged().collect {
                uncheckSelectAllIfAllSelected()
            }
        }

        viewModelScope.launch {
            _allTasks.map { taskListUI -> taskListUI.map { it.id } }.distinctUntilChanged().collect {
                onDismissDeleteModeByAllTasks()
            }
        }
    }

    fun dateSelection() {
        _dateTimeMode.value = DateTimeDialogMode.DATE_MODE
    }

    fun timeSelection() {
        _dateTimeMode.value = DateTimeDialogMode.TIME_MODE
    }

    fun nonDateTimeSelection() {
        _dateTimeMode.value = DateTimeDialogMode.NON_DIALOG_MODE
    }

    fun checkedSelectAll(checked: Boolean) {
        _selectMode.update { it.copy(selectAll = checked) }
    }

    fun deleteMode(checked: Boolean) {
        _selectMode.update { it.copy(isDeleting = checked) }
    }

    fun cleaningSelectedIdsList() {
        _selectMode.update { it.copy(selectedIds = emptyList()) }
    }

    fun taskInSelectedTaskById(taskId: Long): Boolean {
        return taskId in _selectMode.value.selectedIds
    }

    fun onDismissDeleteModeByAllTasks() {
        if ((_allTasks.value.isEmpty()) and (_selectMode.value.isDeleting)) {
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
        _stateAddAndEditTask.update { it.copy(id = id) }
    }

    fun onTitleChange(title: String) {
        _stateAddAndEditTask.update { it.copy(title = title) }
    }

    fun onDateSelected(date: LocalDate?) {
        _stateAddAndEditTask.update { it.copy(date = date) }
    }

    fun onTimeSelected(time: LocalTime?) {
        _stateAddAndEditTask.update { it.copy(time = time) }
    }

    fun selectTaskId(taskId: Long) {
        val selectedIds = _selectMode.value.selectedIds
        _selectMode.update {
            it.copy(
                selectedIds = if (taskId !in selectedIds) selectedIds + taskId
                else selectedIds - taskId
            )
        }
    }

    fun selectAllTaskId() {
        _selectMode.update { current ->
            if (current.selectAll) {
                val allTasks = _allTasks.value.map { it.id }
                current.copy(selectedIds = allTasks)
            } else {
                current.copy(selectedIds = emptyList())
            }
        }
    }

    fun uncheckSelectAllIfAllSelected() {
        val selectedIds = _selectMode.value.selectedIds.sorted()
        val allTasksIds = _allTasks.value.map { it.id }.sorted()
        if (selectedIds == allTasksIds) {
            checkedSelectAll(true)
        } else {
            checkedSelectAll(false)
        }
    }

    fun onEditParamSelected(id: Long, title: String, date: LocalDate?, time: LocalTime?) {
        onIdChange(id = id)
        onTitleChange(title = title)
        onDateSelected(date = date)
        onTimeSelected(time = time)
    }

    fun onTimeDismiss() {
        _stateAddAndEditTask.update {
            TaskState(
                id = _stateAddAndEditTask.value.id,
                title = _stateAddAndEditTask.value.title,
                date = _stateAddAndEditTask.value.date,
                time = null
            )
        }
    }

    fun onDismissDeleteAndEditTask() {
        _stateAddAndEditTask.update { TaskState() }
    }

    fun getFoundTasks(title: String): Flow<TaskFoundForm> {
        if (title.isEmpty()) {
            return flowOf(TaskFoundForm())
        }

        return getFoundTasksUseCase(title).map { taskListDomain ->
            val tasksUI = taskListDomain.map { it.toUI() }
            val sortedByMatchIndex = tasksUI.sortedBy { it.title.indexOf(title.lowercase()) }
            val grouped = mapOf(title.lowercase() to sortedByMatchIndex)
            TaskFoundForm(matchesTitle = grouped)
        }
    }

    fun createTask() {
        if (_stateAddAndEditTask.value.title.isNotBlank()) {
            viewModelScope.launch {
                createTaskUseCase(
                    taskDomain = TaskDomain(
                        title = _stateAddAndEditTask.value.title.trim(),
                        date = _stateAddAndEditTask.value.date,
                        notification = _stateAddAndEditTask.value.time
                    )
                )
                _stateAddAndEditTask.update { TaskState() }
            }
        }
    }

    fun editTask() {
        if (_stateAddAndEditTask.value.title.isNotEmpty()) {
            viewModelScope.launch {
                updateTaskUseCase(
                    taskDomain = TaskDomain(
                        id = _stateAddAndEditTask.value.id ?: return@launch,
                        title = _stateAddAndEditTask.value.title,
                        date = _stateAddAndEditTask.value.date,
                        notification = _stateAddAndEditTask.value.time
                    )
                )
                _stateAddAndEditTask.update { TaskState() }
            }
        }
    }

    fun deleteTask() {
        if (_selectMode.value.selectedIds.isNotEmpty()) {
            viewModelScope.launch {
                _selectMode.value.selectedIds.forEach { taskId ->
                    deleteTaskUseCase(taskId)
                }
            }
            cleaningSelectedIdsList()
        }
    }

    fun completingTask(taskId: Long) {
        viewModelScope.launch {
            completingTaskUseCase(taskId = taskId)
        }
    }
}

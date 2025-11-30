package com.example.smartday.ui.models.states

import com.example.smartday.core.models.task.TaskModel
import java.time.LocalDate

sealed class TaskListItem {
    sealed class Title() : TaskListItem() {
        data class IntTitle(val title: Int) : TaskListItem()
        data class DateTitle(val title: LocalDate) : TaskListItem()
    }

    data class TaskList(val taskList: List<TaskModel>) : TaskListItem()
}

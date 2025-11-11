package com.example.smartday.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartday.presentation.main.TaskViewModel
import com.example.smartday.presentation.models.TaskUI
import com.example.smartday.presentation.utils.toDisplayString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    taskUI: TaskUI,
    taskViewModel: TaskViewModel,
    matchesTitle: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    var checked by remember { mutableStateOf(taskUI.isCompleted) }
    val coroutine = rememberCoroutineScope()

    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

    Row(
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(8.dp)
            )
            .combinedClickable(
                onLongClick = if (matchesTitle == null) onLongClick else null
            ) {
                if ((!taskUI.isCompleted) and (!taskDeleteMode.isDeleting)) {
                    onClick?.let {
                        taskViewModel.onEditParamSelected(
                            id = taskUI.id, title = taskUI.title, date = taskUI.date, time = taskUI.notification
                        )
                        it()
                    }
                }

                if ((taskDeleteMode.isDeleting) and (matchesTitle == null)) {
                    onClick?.let {
                        taskViewModel.selectTaskId(taskUI.id)
                        it()
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                if (!taskDeleteMode.isDeleting) {
                    Checkbox(
                        checked = checked, onCheckedChange = { check ->
                            if (check) {
                                coroutine.launch {
                                    checked = true
                                    delay(200)
                                    taskViewModel.completingTask(taskId = taskUI.id)
                                }
                            }
                        }, colors = colors(
                            checkmarkColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
                            checkedColor = MaterialTheme.colorScheme.surfaceContainer,
                        )
                    )
                } else {
                    Checkbox(
                        checked = taskViewModel.taskInSelectedTaskById(taskUI.id), onCheckedChange = {
                            taskViewModel.selectTaskId(taskUI.id)
                        }, colors = colors(
                            checkmarkColor = MaterialTheme.colorScheme.surface,
                            uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f),
                            checkedColor = MaterialTheme.colorScheme.primary,
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.width(6.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                ) {
                    val clipTextColor = MaterialTheme.colorScheme.onSurface.copy(
                        if (taskUI.isCompleted) 0.5f else 1f
                    )
                    val matchesTextColor = MaterialTheme.colorScheme.primary
                    Text(
                        text = buildAnnotatedString {
                            append(taskUI.title)
                            addStyle(
                                style = SpanStyle(color = clipTextColor),
                                start = 0,
                                end = taskUI.title.length
                            )
                            if (matchesTitle != null) {
                                val start = taskUI.title.indexOf(matchesTitle, ignoreCase = true)
                                addStyle(
                                    style = SpanStyle(color = matchesTextColor),
                                    start = start,
                                    end = start + matchesTitle.length
                                )
                            }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = if (taskUI.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                }
                if ((taskUI.date != null) and ((taskUI.isOverdue) or (taskUI.isCompleted))) {
                    Text(
                        text = taskUI.date!!.toDisplayString(locale, LocalContext.current),
                        style = MaterialTheme.typography.labelMedium,
                        color = if ((taskUI.isOverdue) and (taskUI.isCompleted)) MaterialTheme.colorScheme.onSurface.copy(
                            0.5f
                        ) else if (taskUI.isOverdue) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
            }
        }
        if (taskUI.notification != null) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = taskUI.notification.format(DateTimeFormatter.ofPattern("HH : mm")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

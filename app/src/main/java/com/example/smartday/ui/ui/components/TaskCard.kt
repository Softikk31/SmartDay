package com.example.smartday.ui.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.Red
import com.example.smartday.ui.ui.theme.Yellow
import com.example.smartday.ui.utils.toDisplayString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: TaskModel,
    taskViewModel: TaskViewModel,
    matchesTitle: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val taskDeleteMode by taskViewModel.deleteMode.collectAsState()

    var checked by rememberSaveable(task.isCompleted) { mutableStateOf(task.isCompleted) }
    val coroutine = rememberCoroutineScope()

    val locale = remember {
        when (Locale.getDefault().language) {
            "ru" -> Locale.forLanguageTag("ru")
            else -> Locale.ENGLISH
        }
    }

    val context = LocalContext.current

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
                if (taskDeleteMode.isDeleting) {
                    taskViewModel.selectTaskId(task.id)
                }

                onClick?.let {
                    if ((!task.isCompleted) and (!taskDeleteMode.isDeleting)) {
                        taskViewModel.onEditParamSelected(
                            id = task.id,
                            title = task.title,
                            repetition = task.repetition,
                            priority = task.priority,
                            date = task.date,
                            time = task.notification
                        )
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
                                    taskViewModel.completingTask(taskId = task.id, context = context)
                                }
                            }
                        }, colors = when (task.priority) {
                            TaskPriority.NULL -> colors(
                                checkmarkColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    0.5f
                                ),
                                checkedColor = MaterialTheme.colorScheme.surfaceContainer,
                            )

                            TaskPriority.LOW -> colors(
                                checkmarkColor = Blue,
                                uncheckedColor = Blue.copy(alpha = 0.6f),
                                checkedColor = Blue.copy(alpha = 0.12f)
                            )

                            TaskPriority.MEDIUM -> colors(
                                checkmarkColor = Yellow,
                                uncheckedColor = Yellow.copy(alpha = 0.7f),
                                checkedColor = Yellow.copy(alpha = 0.16f)
                            )

                            TaskPriority.HIGH -> colors(
                                checkmarkColor = Red,
                                uncheckedColor = Red.copy(alpha = 0.8f),
                                checkedColor = Red.copy(alpha = 0.2f)
                            )
                        }
                    )
                } else {
                    Checkbox(
                        checked = taskViewModel.taskInSelectedTaskById(task.id),
                        onCheckedChange = {
                            taskViewModel.selectTaskId(task.id)
                        },
                        colors = colors(
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
                        .width(160.dp)
                ) {
                    val clipTextColor = MaterialTheme.colorScheme.onSurface.copy(
                        if (task.isCompleted) 0.5f else 1f
                    )
                    val matchesTextColor = MaterialTheme.colorScheme.primary
                    Text(
                        text = buildAnnotatedString {
                            append(task.title)
                            addStyle(
                                style = SpanStyle(color = clipTextColor),
                                start = 0,
                                end = task.title.length
                            )
                            if (matchesTitle != null) {
                                val start = task.title.indexOf(matchesTitle, ignoreCase = true)
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
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                }

                if (((task.date != null) and ((task.isOverdue) or (task.isCompleted))) and (task.repetition != TaskRepetitionModel())) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = task.date!!.toDisplayString(locale, LocalContext.current),
                            style = MaterialTheme.typography.labelMedium,
                            color = if ((task.isOverdue) and (task.isCompleted)) MaterialTheme.colorScheme.onSurface.copy(
                                0.5f
                            ) else if (task.isOverdue) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                        )

                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if ((task.date != null) and ((task.isOverdue) or (task.isCompleted))) {
                    Text(
                        text = task.date!!.toDisplayString(locale, LocalContext.current),
                        style = MaterialTheme.typography.labelMedium,
                        color = if ((task.isOverdue) and (task.isCompleted)) MaterialTheme.colorScheme.onSurface.copy(
                            0.5f
                        ) else if (task.isOverdue) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                } else if (task.repetition != TaskRepetitionModel()) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.ic_refresh),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (task.notification != null) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = task.notification!!.format(DateTimeFormatter.ofPattern("HH : mm")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}
package com.example.smartday.ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartday.R
import com.example.smartday.core.enums.TaskPriority
import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.ui.main.view_models.TaskViewModel
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.Green
import com.example.smartday.ui.ui.theme.RedPrimary
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
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
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
                    if ((!task.isCompleted) && (!taskDeleteMode.isDeleting)) {
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
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Row {
            Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                if (!taskDeleteMode.isDeleting) {
                    if (!task.isCompleted) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(
                                    when (task.priority) {
                                        TaskPriority.NULL -> Color.Transparent
                                        TaskPriority.LOW -> Blue.copy(0.1f)
                                        TaskPriority.MEDIUM -> Yellow.copy(0.1f)
                                        TaskPriority.HIGH -> RedPrimary.copy(0.1f)
                                    }
                                )
                        )
                    }

                    Checkbox(
                        checked = checked,
                        onCheckedChange = { check ->
                            if (check) {
                                coroutine.launch {
                                    checked = true
                                    delay(200)
                                    taskViewModel.completingTask(task.id, context)
                                }
                            }
                        },
                        colors = when (task.priority) {
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
                                checkmarkColor = RedPrimary,
                                uncheckedColor = RedPrimary.copy(alpha = 0.8f),
                                checkedColor = RedPrimary.copy(alpha = 0.2f)
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

            Column(
                modifier = Modifier.padding(vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Box(modifier = Modifier.width(300.dp)) {
                    val clipColor = MaterialTheme.colorScheme.onSurface.copy(
                        if (task.isCompleted) 0.5f else 1f
                    )
                    val matchColor = MaterialTheme.colorScheme.primary

                    Text(
                        text = buildAnnotatedString {
                            append(task.title)
                            addStyle(
                                SpanStyle(color = clipColor),
                                0,
                                task.title.length
                            )
                            if (matchesTitle != null) {
                                val start = task.title.indexOf(matchesTitle, ignoreCase = true)
                                if (start >= 0) {
                                    addStyle(
                                        SpanStyle(color = matchColor),
                                        start,
                                        start + matchesTitle.length
                                    )
                                }
                            }
                        },
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = if (task.isCompleted)
                                TextDecoration.LineThrough
                            else
                                TextDecoration.None
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val baseColor = when {
                        task.isCompleted -> MaterialTheme.colorScheme.onSurfaceVariant
                        task.isOverdue -> MaterialTheme.colorScheme.error
                        !task.isOverdue -> Green
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    if (task.date != null) {
                        Icon(
                            modifier = Modifier.size(15.5.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar),
                            contentDescription = null,
                            tint = baseColor
                        )

                        if (task.isCompleted or task.isOverdue) {
                            Text(
                                task.date!!.toDisplayString(locale, context),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                                color = baseColor
                            )
                        }
                    }

                    if (task.notification != null) {
                        Text(
                            task.notification!!.format(DateTimeFormatter.ofPattern("HH : mm")),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                            color = baseColor
                        )
                    }

                    if (task.repetition != TaskRepetitionModel()) {
                        Icon(
                            modifier = Modifier.size(13.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_repeat),
                            contentDescription = null,
                            tint = baseColor
                        )
                    }
                }

//                if (task.category != null) {
//                    Row(
//                        modifier = Modifier.width(300.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        TextCategory(task.category)
//                    }
//                }
            }
        }
    }
}


//const val HASHTAG = "#"
//
//@Composable
//fun TextCategory(text: String) {
//    Text(
//        text = HASHTAG + text,
//        style = MaterialTheme.typography.labelMedium,
//        color = MaterialTheme.colorScheme.onSurfaceVariant,
//        maxLines = 1,
//        overflow = TextOverflow.Ellipsis
//    )
//}

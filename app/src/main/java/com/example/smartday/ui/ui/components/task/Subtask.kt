package com.example.smartday.ui.ui.components.task

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartday.R
import com.example.smartday.core.models.task.SubtaskModel
import com.example.smartday.ui.main.view_models.TaskViewModel

@Composable
fun Subtask(subtask: SubtaskModel, taskViewModel: TaskViewModel) {
    val titleSubtask = key(subtask.id) {
        rememberTextFieldState(initialText = subtask.title)
    }

    val context = LocalContext.current

    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }


    LaunchedEffect(titleSubtask.text) {
        taskViewModel.editSubtask(
            subtask.copy(
                id = subtask.id,
                title = titleSubtask.text.toString(),
                isCompleted = subtask.isCompleted
            )
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(start = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TriStateCheckbox(
                interactionSource = remember { MutableInteractionSource() },
                state = ToggleableState(subtask.isCompleted),
                colors = CheckboxDefaults.colors(
                    checkmarkColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        0.5f
                    ),
                    checkedColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                onClick = {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            255
                        )
                    )
                    taskViewModel.editSubtask(
                        subtask.copy(
                            id = subtask.id,
                            title = subtask.title,
                            isCompleted = !subtask.isCompleted
                        )
                    )
                }
            )

            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                state = titleSubtask,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorator = TextFieldDefaults.decorator(
                    state = titleSubtask,
                    enabled = true,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    outputTransformation = null,
                    interactionSource = remember { MutableInteractionSource() },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.subtask),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    contentPadding = PaddingValues(0.dp),
                    colors = colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.primary.copy(
                                0.4f
                            )
                        )
                    )
                )
            )
        }

        Row {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            taskViewModel.deleteSubtask(subtask)
                        }
                    ),
                imageVector = ImageVector.vectorResource(R.drawable.ic_x),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
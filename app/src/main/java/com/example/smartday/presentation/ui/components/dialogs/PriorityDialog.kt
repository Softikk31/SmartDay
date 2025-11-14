package com.example.smartday.presentation.ui.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.smartday.presentation.ui.theme.SmartDayTheme

@Composable
fun PriorityDialog() {
    SmartDayTheme {
        CustomDialog(
            title = "Priority",
            titleStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            titleColor = MaterialTheme.colorScheme.onSurface,
            onSelected = {

            },
            onDismiss = {

            }
        ) {

        }
    }
}
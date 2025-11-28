package com.example.smartday.ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.smartday.R

@Composable
fun CustomFloatActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorContainer: Color = MaterialTheme.colorScheme.primary,
    colorContent: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(bottom = CustomFloatActionButtonBottomPadding)
            .size(56.dp)
            .background(
                color = colorContainer, shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                enabled = enabled, interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_plus),
            contentDescription = null,
            tint = colorContent
        )
    }
}

private val CustomFloatActionButtonBottomPadding: Dp = 80.dp
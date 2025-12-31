package com.example.smartday.ui.ui.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.example.smartday.R

@Composable
fun CustomActionButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = ImageVector.vectorResource(R.drawable.ic_plus),
    enabled: Boolean = true,
    colorContainer: Color = MaterialTheme.colorScheme.primary,
    colorContent: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .background(
                color = colorContainer, shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = colorContent
        )
    }
}

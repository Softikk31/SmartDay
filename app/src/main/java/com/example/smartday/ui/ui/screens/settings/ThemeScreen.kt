package com.example.smartday.ui.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.ui.main.view_models.ThemeViewModel
import com.example.smartday.ui.ui.components.CustomScaffoldTopBar
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.GreenPrimary
import com.example.smartday.ui.ui.theme.Orange
import com.example.smartday.ui.ui.theme.Purple
import com.example.smartday.ui.ui.theme.RedPrimary
import com.example.smartday.ui.ui.theme.Yellow

@Composable
fun ThemeScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel
) {
    val theme by themeViewModel.theme.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    val systemTheme = isSystemInDarkTheme()

    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                icon = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                text = stringResource(R.string.theme_title)
            ) {
                val currentState = lifecycleOwner.lifecycle.currentState
                if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    navController.popBackStack()
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(36.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.width(248.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.system_theme),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.system_theme_description),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = theme.systemTheme,
                    onCheckedChange = { enabled ->
                        themeViewModel.editSystem(enabled)

                        if (enabled) {
                            themeViewModel.editTheme(systemTheme)
                        }
                    },
                    colors = colors(
                        checkedThumbColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            val currentSystemTheme = isSystemInDarkTheme()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (theme.systemTheme) {
                            themeViewModel.editSystem(false)
                            themeViewModel.editTheme(!currentSystemTheme)
                        } else {
                            themeViewModel.editTheme(!theme.isDarkMode)
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = ImageVector.vectorResource(
                        when {
                            theme.systemTheme -> if (systemTheme) R.drawable.ic_sun else R.drawable.ic_moon
                            theme.isDarkMode -> R.drawable.ic_sun
                            else -> R.drawable.ic_moon
                        }
                    ),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(
                        when {
                            theme.systemTheme -> if (systemTheme) {
                                R.string.switch_to_light_theme
                            } else {
                                R.string.switch_to_dark_theme
                            }

                            theme.isDarkMode -> R.string.switch_to_light_theme
                            else -> R.string.switch_to_dark_theme
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(R.string.primary_color),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.YELLOW,
                        color = Yellow,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.YELLOW)
                        }
                    )
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.RED,
                        color = RedPrimary,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.RED)
                        }
                    )
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.BLUE,
                        color = Blue,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.BLUE)
                        }
                    )
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.ORANGE,
                        color = Orange,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.ORANGE)
                        }
                    )
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.GREEN,
                        color = GreenPrimary,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.GREEN)
                        }
                    )
                    CircleSelectPrimary(
                        checked = theme.primaryColor == ThemePrimaryColors.PURPLE,
                        color = Purple,
                        onClick = {
                            themeViewModel.editPrimary(ThemePrimaryColors.PURPLE)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CircleSelectPrimary(
    checked: Boolean,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = if (checked) color else Color.Transparent,
                shape = CircleShape
            )
            .padding(4.dp)
            .size(46.dp)
            .clip(shape = CircleShape)
            .background(color)
            .clickable(
                onClick = onClick
            )
    )
}
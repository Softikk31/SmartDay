package com.example.smartday.ui.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.smartday.ui.ui.components.CustomScaffoldTopBar
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.theme.Blue
import com.example.smartday.ui.ui.theme.Green
import com.example.smartday.ui.ui.theme.Orange
import com.example.smartday.ui.ui.theme.Purple
import com.example.smartday.ui.ui.theme.Red
import com.example.smartday.ui.ui.theme.Yellow

@Composable
fun ThemeScreen(
    navController: NavHostController
) {
    var checkedPrimaryColor by rememberSaveable {
        mutableStateOf(ThemePrimaryColors.YELLOW)
    }

    var checkedSystemTheme by rememberSaveable {
        mutableStateOf(true)
    }

    var themeMode by rememberSaveable {
        mutableStateOf(ThemeMode.LIGHT)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

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
                    checked = checkedSystemTheme,
                    onCheckedChange = {
                        checkedSystemTheme = !checkedSystemTheme
                    },
                    colors = colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        themeMode =
                            if (themeMode == ThemeMode.LIGHT) ThemeMode.DARK else ThemeMode.LIGHT
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = ImageVector.vectorResource(if (themeMode == ThemeMode.LIGHT) R.drawable.ic_moon else R.drawable.ic_sun),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(if (themeMode == ThemeMode.LIGHT) R.string.switch_to_dark_theme else R.string.switch_to_light_theme),
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
                        checked = checkedPrimaryColor == ThemePrimaryColors.YELLOW,
                        color = Yellow,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.YELLOW
                        }
                    )
                    CircleSelectPrimary(
                        checked = checkedPrimaryColor == ThemePrimaryColors.RED,
                        color = Red,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.RED
                        }
                    )
                    CircleSelectPrimary(
                        checked = checkedPrimaryColor == ThemePrimaryColors.BLUE,
                        color = Blue,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.BLUE
                        }
                    )
                    CircleSelectPrimary(
                        checked = checkedPrimaryColor == ThemePrimaryColors.ORANGE,
                        color = Orange,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.ORANGE
                        }
                    )
                    CircleSelectPrimary(
                        checked = checkedPrimaryColor == ThemePrimaryColors.GREEN,
                        color = Green,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.GREEN
                        }
                    )
                    CircleSelectPrimary(
                        checked = checkedPrimaryColor == ThemePrimaryColors.PURPLE,
                        color = Purple,
                        onClick = {
                            checkedPrimaryColor = ThemePrimaryColors.PURPLE
                        }
                    )
                }
            }
        }
    }
}

enum class ThemePrimaryColors {
    YELLOW,
    RED,
    BLUE,
    ORANGE,
    GREEN,
    PURPLE
}

enum class ThemeMode {
    LIGHT,
    DARK
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
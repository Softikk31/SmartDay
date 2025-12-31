package com.example.smartday.ui.ui.screens.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.ThemeViewModel
import com.example.smartday.ui.ui.components.bars.CustomTopBar
import com.example.smartday.ui.ui.components.scaffold.CustomScaffoldTopBar
import com.example.smartday.ui.ui.navigation.Theme

@Composable
fun SettingsScreen(
    navController: NavHostController, themeViewModel: ThemeViewModel
) {
    val theme by themeViewModel.theme.collectAsState()

    val context = LocalContext.current

    CustomScaffoldTopBar(
        topBar = {
            CustomTopBar(
                text = stringResource(R.string.title_settings),
            )
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingItem(
                    title = stringResource(R.string.theme_title),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_theme),
                    value = stringResource(
                        when {
                            theme.systemTheme -> {
                                R.string.system_theme
                            }

                            else -> {
                                if (theme.isDarkMode) {
                                    R.string.dark_theme
                                } else {
                                    R.string.light_theme
                                }
                            }
                        }
                    )
                ) {
                    navController.navigate(Theme)
                }
            }

            item {
                SettingItem(
                    title = stringResource(R.string.title_language),
                    value = stringResource(R.string.language_settings_label),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_language),
                ) {
                    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    } else {
                        Intent(Settings.ACTION_LOCALE_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    }
                    context.startActivity(intent)

                }
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String, imageVector: ImageVector, value: String? = null, onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

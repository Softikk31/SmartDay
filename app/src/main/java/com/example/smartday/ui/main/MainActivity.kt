package com.example.smartday.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.ThemeViewModel
import com.example.smartday.ui.ui.navigation.NavGraph
import com.example.smartday.ui.ui.theme.SmartDayTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val themeViewModel: ThemeViewModel = koinViewModel()
            val theme by themeViewModel.theme.collectAsState()

            val darkTheme = if (theme.systemTheme) isSystemInDarkTheme() else theme.isDarkMode
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Transparent.toArgb(),
                    Transparent.toArgb(),
                    detectDarkMode = { darkTheme }),
                navigationBarStyle = SystemBarStyle.light(
                    Transparent.toArgb(),
                    Transparent.toArgb()
                ),
            )
            RequestNotificationPermission()
            SmartDayTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current

    val text = stringResource(R.string.notification_permission_denied)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(
                    context,
                    text,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

package com.example.smartday.ui.main

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.smartday.R
import com.example.smartday.ui.main.view_models.MainViewModel
import com.example.smartday.ui.ui.navigation.NavGraph
import com.example.smartday.ui.ui.theme.SmartDayTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Transparent.toArgb(), Transparent.toArgb()),
            navigationBarStyle = SystemBarStyle.light(Transparent.toArgb(), Transparent.toArgb()),
        )
        installSplashScreen()
        setContent {
            RequestNotificationPermission()
            val viewModel: MainViewModel = koinViewModel()

            SmartDayTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current

    val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(
                    context,
                    context.getString(R.string.notification_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    notificationPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                launcher.launch(notificationPermission)
            }
        }

        if (!alarmManager.canScheduleExactAlarms()) {
            context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
    }
}

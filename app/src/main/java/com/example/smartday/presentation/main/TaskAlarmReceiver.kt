package com.example.smartday.presentation.main

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.smartday.R
import com.example.smartday.app.App
import com.example.smartday.domain.usecase.OverdueTaskUseCase
import com.example.smartday.presentation.models.TaskUI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.time.LocalDateTime

const val ONE_HOUR_MS = 60 * 60 * 1000L

class TaskAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val overdueTaskUseCase by inject<OverdueTaskUseCase>()

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("task_id", 0L)
        val taskTitle = intent.getStringExtra("task_title") ?: ""
        val notificationType = intent.getStringExtra("notification_type")

        when (notificationType) {
            NotificationType.OVERDUE.name -> {
                CoroutineScope(Dispatchers.IO).launch {
                    overdueTaskUseCase(taskId)
                }
            }
        }

        val (title, text) = when (notificationType) {
            NotificationType.REMINDER.name -> {
                "Напоминание!" to "Не забудь выполнить задачу: «$taskTitle»"
            }

            NotificationType.OVERDUE.name -> {
                "Задача просрочена!" to "«$taskTitle» не была выполнена вовремя."
            }

            else -> return
        }

        val notification = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(taskId.hashCode(), notification)
    }
}

fun scheduleTaskAlarm(
    context: Context,
    overdueTaskUseCase: OverdueTaskUseCase,
    task: TaskUI
) {
    val now = LocalDateTime.now()

    val taskDateTime = when {
        task.date != null && task.notification == null -> task.date.atStartOfDay().plusDays(1)
        task.date != null && task.notification != null -> LocalDateTime.of(task.date, task.notification)
        else -> null
    } ?: return

    val delay = Duration.between(now, taskDateTime).toMillis()
    if (delay <= 0) {
        CoroutineScope(Dispatchers.IO).launch {
            overdueTaskUseCase(task.id)
        }
        return
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (delay > ONE_HOUR_MS) {
        val reminderTime = System.currentTimeMillis() + delay - ONE_HOUR_MS
        val reminderIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
            dataWork(task.id, task.title, NotificationType.REMINDER.name)
        }

        val reminderPendingIntent = PendingIntent.getBroadcast(
            context,
            getRequestCode(task.id, NotificationType.REMINDER),
            reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime,
                    reminderPendingIntent
                )
            } else {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                context.startActivity(intent)
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, reminderPendingIntent)
        }
    }

    val overdueTime = System.currentTimeMillis() + delay
    val overdueIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
        dataWork(task.id, task.title, NotificationType.OVERDUE.name)
    }

    val overduePendingIntent = PendingIntent.getBroadcast(
        context,
        getRequestCode(task.id, NotificationType.OVERDUE),
        overdueIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        overdueTime,
        overduePendingIntent
    )
}

fun Intent.dataWork(id: Long, title: String, type: String) {
    putExtra("task_id", id)
    putExtra("task_title", title)
    putExtra("notification_type", type)
}

fun getRequestCode(taskId: Long, type: NotificationType): Int {
    return (taskId * 10 + type.ordinal).toInt()
}

enum class NotificationType {
    REMINDER,
    OVERDUE
}

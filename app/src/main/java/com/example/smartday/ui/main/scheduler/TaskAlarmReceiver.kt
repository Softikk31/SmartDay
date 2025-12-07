package com.example.smartday.ui.main.scheduler

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.smartday.R
import com.example.smartday.core.enums.TaskTypeRepetition
import com.example.smartday.core.enums.TaskTypeRepetitionCustom
import com.example.smartday.core.enums.TaskTypeRepetitionSystem
import com.example.smartday.core.models.task.TaskModel
import com.example.smartday.core.models.task.repetition.TaskRepetitionModel
import com.example.smartday.core.models.task.repetition.values.DaysOfWeekRepetition
import com.example.smartday.core.models.task.repetition.values.RepetitionValue
import com.example.smartday.domain.usecase.task.GetTaskUseCase
import com.example.smartday.domain.usecase.task.OverdueTaskUseCase
import com.example.smartday.domain.usecase.task.UpdateTaskUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

const val ONE_HOUR_MS = 60 * 60 * 1000L

class TaskAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val overdueTaskUseCase by inject<OverdueTaskUseCase>()
    private val getTaskUseCase by inject<GetTaskUseCase>()
    private val updateTaskUseCase by inject<UpdateTaskUseCase>()

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("task_id", 0L)
        val taskTitle = intent.getStringExtra("task_title") ?: ""
        val taskRepetitionEncode =
            intent.getStringExtra("task_repetition") ?: Json.encodeToString(TaskRepetitionModel())
        val taskRepetition = Json.decodeFromString<TaskRepetitionModel>(taskRepetitionEncode)
        val notificationType = intent.getStringExtra("notification_type")

        when (notificationType) {
            NotificationType.OVERDUE.name -> {
                if (taskRepetition == TaskRepetitionModel()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        overdueTaskUseCase(taskId)
                    }
                }
            }
        }

        val (title, text) = when (notificationType) {
            NotificationType.REMINDER.name -> {
                context.getString(R.string.reminder_title_notification) to context.getString(
                    R.string.reminder_text_notification,
                    taskTitle
                )
            }

            NotificationType.OVERDUE.name -> {
                context.getString(R.string.overdue_title_notification) to context.getString(
                    R.string.overdue_text_notification,
                    taskTitle
                )
            }

            else -> return
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

        val channel = NotificationChannel(
            context.getString(R.string.channel_id),
            context.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_HIGH
        )

        channel.setShowBadge(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        manager.createNotificationChannel(channel)
        manager.notify(taskId.hashCode(), notification)

        if (taskRepetition != TaskRepetitionModel()) {
            CoroutineScope(Dispatchers.IO).launch {
                scheduleTaskAlarm(
                    context,
                    overdueTaskUseCase,
                    updateTaskUseCase,
                    getTaskUseCase(taskId)
                )
            }
        }
    }
}

fun scheduleTaskAlarm(
    context: Context,
    overdueTaskUseCase: OverdueTaskUseCase,
    updateTaskUseCase: UpdateTaskUseCase,
    task: TaskModel
) {
    val now = LocalDateTime.now()

    val initialDateTime = when {
        task.date != null && task.notification == null ->
            task.date!!.atStartOfDay()

        task.date != null && task.notification != null ->
            LocalDateTime.of(task.date, task.notification)

        else -> return
    }

    val nextTrigger = if (task.repetition == TaskRepetitionModel()) {
        initialDateTime
    } else {
        if (initialDateTime > now) {
            initialDateTime
        } else {
            calculateNextTriggerTime(initialDateTime, task.repetition) ?: return
        }
    }

    val delay = Duration.between(now, nextTrigger).toMillis()
    if (delay <= 0) {
        CoroutineScope(Dispatchers.IO).launch { overdueTaskUseCase(task.id) }
        return
    }

    CoroutineScope(Dispatchers.IO).launch {
        updateTaskUseCase(
            task.copy(
                date = nextTrigger.toLocalDate(),
                notification = nextTrigger.toLocalTime(),
                isCompleted = false
            )
        )
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (delay > ONE_HOUR_MS) {
        val reminderTime = System.currentTimeMillis() + delay - ONE_HOUR_MS
        val reminderIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
            dataWork(
                task.id,
                task.title,
                Json.encodeToString(task.repetition),
                NotificationType.REMINDER.name
            )
        }

        val reminderPendingIntent = PendingIntent.getBroadcast(
            context,
            getRequestCode(task.id, NotificationType.REMINDER),
            reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderTime,
            reminderPendingIntent
        )
    }

    val overdueTime = System.currentTimeMillis() + delay
    val overdueIntent = Intent(context, TaskAlarmReceiver::class.java).apply {
        dataWork(
            task.id,
            task.title,
            Json.encodeToString(task.repetition),
            NotificationType.OVERDUE.name
        )
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


private fun calculateNextTriggerTime(
    from: LocalDateTime,
    repetition: TaskRepetitionModel
): LocalDateTime? {
    val timeOfDay = from.toLocalTime().takeIf { it != LocalTime.MIDNIGHT }

    val nextDate = when (val type = repetition.type) {
        is TaskTypeRepetition.OnSystemTypeRepetition -> when (type.enumClass) {
            TaskTypeRepetitionSystem.EVERY_DAY -> from.toLocalDate().plusDays(1)
            TaskTypeRepetitionSystem.EVERY_WEEK_ON -> from.toLocalDate().plusWeeks(1)
            TaskTypeRepetitionSystem.EVERY_WORKDAY -> from.toLocalDate().nextWorkdayDate()
            TaskTypeRepetitionSystem.EVERY_MONTH_DATE -> from.toLocalDate().plusMonths(1)
            TaskTypeRepetitionSystem.EVERY_YEAR_DATE -> from.toLocalDate().plusYears(1)
            else -> return null
        }

        is TaskTypeRepetition.OnCustomTypeRepetition -> when (type.enumClass) {
            TaskTypeRepetitionCustom.DAY -> from.toLocalDate().plusDays(repetition.counter.toLong())
            TaskTypeRepetitionCustom.WEEK -> {
                from.toLocalDate().nextValueDaysWeekDate(repetition.value, repetition.counter)
            }

            TaskTypeRepetitionCustom.MONTH -> from.toLocalDate()
                .plusMonths(repetition.counter.toLong())

            TaskTypeRepetitionCustom.YEAR -> from.toLocalDate()
                .plusYears(repetition.counter.toLong())
        }
    }

    return if (nextDate != null) {
        if (timeOfDay != null) {
            nextDate.atTime(timeOfDay)
        } else {
            nextDate.atStartOfDay()
        }
    } else {
        return null
    }
}


fun Intent.dataWork(id: Long, title: String, repetition: String, type: String) {
    putExtra("task_id", id)
    putExtra("task_title", title)
    putExtra("task_repetition", repetition)
    putExtra("notification_type", type)
}

fun getRequestCode(taskId: Long, type: NotificationType): Int {
    return (taskId * 10 + type.ordinal).toInt()
}

enum class NotificationType {
    REMINDER,
    OVERDUE
}

fun LocalDate.nextWorkdayDate(): LocalDate {
    var date = this.plusDays(1)
    val weekend = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

    while (date.dayOfWeek in weekend) {
        date = date.plusDays(1)
    }

    return date
}

fun LocalDate.nextValueDaysWeekDate(
    selectedDays: RepetitionValue?,
    counter: Int = 1
): LocalDate? {

    when (selectedDays) {
        is DaysOfWeekRepetition -> {
            var date = this.plusDays(1)
            var selectedDaysFilter =
                selectedDays.value.sortedBy { it.value }.filter { it.value > this.dayOfWeek.value }

            if (selectedDaysFilter.isEmpty()) {
                date = if (counter > 1) date.plusWeeks(counter.toLong())
                    .with(DayOfWeek.MONDAY) else date.with(
                    TemporalAdjusters.next(DayOfWeek.MONDAY)
                )
                selectedDaysFilter =
                    selectedDays.value.sortedBy { it.value }
                        .filter { it.value >= date.dayOfWeek.value }
            }

            while (date.dayOfWeek !in selectedDaysFilter) {
                date = date.plusDays(1)
            }

            return date
        }

        else -> return null
    }
}

fun cancelTaskAlarm(context: Context, taskId: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val reminderIntent = Intent(context, TaskAlarmReceiver::class.java)
    val overdueIntent = Intent(context, TaskAlarmReceiver::class.java)

    val reminderPending = PendingIntent.getBroadcast(
        context,
        getRequestCode(taskId, NotificationType.REMINDER),
        reminderIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val overduePending = PendingIntent.getBroadcast(
        context,
        getRequestCode(taskId, NotificationType.OVERDUE),
        overdueIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.cancel(reminderPending)
    alarmManager.cancel(overduePending)
}

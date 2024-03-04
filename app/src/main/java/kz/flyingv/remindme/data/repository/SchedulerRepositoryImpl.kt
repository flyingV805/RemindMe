package kz.flyingv.remindme.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kz.flyingv.remindme.data.preferences.PreferencesKeys
import kz.flyingv.remindme.data.scheduler.RemindScheduler
import kz.flyingv.remindme.domain.entity.ReminderTime
import kz.flyingv.remindme.data.scheduler.RemindSchedulerImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SchedulerRepositoryImpl: SchedulerRepository, KoinComponent {

    private val context: Context by inject()
    private val preferences: SharedPreferences by inject()
    private val scheduler: RemindScheduler by inject()

    override fun isSchedulerPermissionsAvailable(): Boolean = scheduler.isPermissionsAvailable()

    override fun currentRemindTime(): ReminderTime {

        val selectedHour = preferences.getInt(PreferencesKeys.remindHour, PreferencesKeys.defaultRemindHour)
        val remindMinute = preferences.getInt(PreferencesKeys.remindMinute, PreferencesKeys.defaultRemindMinute)

        return ReminderTime(
            hour = selectedHour,
            minute = remindMinute
        )
    }

    override fun initReminderIfNeeded() {
        scheduler.startIfNotSet(currentRemindTime())
    }

    override fun reschedule(newTime: ReminderTime) {
        scheduler.reschedule(newTime)
        preferences.edit()
            .putInt(PreferencesKeys.remindHour, newTime.hour)
            .putInt(PreferencesKeys.remindMinute, newTime.minute)
            .apply()
    }

    override fun updateScheduler() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        val remindTime = currentRemindTime()

        dueDate.set(Calendar.HOUR_OF_DAY, remindTime.hour)
        dueDate.set(Calendar.MINUTE, remindTime.minute)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {dueDate.add(Calendar.HOUR_OF_DAY, 24)}

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        /*val dailyWorkRequest = OneTimeWorkRequestBuilder<NotificatorWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(RemindSchedulerImpl.reminderTag)
            .build()

        WorkManager.getInstance(context).enqueue(dailyWorkRequest)*/
    }

}
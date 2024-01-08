package kz.flyingv.remindme.data.repository

import android.content.SharedPreferences
import kz.flyingv.remindme.data.preferences.PreferencesKeys
import kz.flyingv.remindme.domain.entity.ReminderTime
import kz.flyingv.remindme.data.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SchedulerRepositoryImpl: SchedulerRepository, KoinComponent {

    private val preferences: SharedPreferences by inject()
    private val scheduler: RemindScheduler by inject()

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

}
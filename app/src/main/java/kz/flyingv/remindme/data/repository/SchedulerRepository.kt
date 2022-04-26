package kz.flyingv.remindme.data.repository

import android.content.SharedPreferences
import kz.flyingv.remindme.data.model.RemindTime
import kz.flyingv.remindme.utils.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface SchedulerRepository {

    fun currentRemindTime(): RemindTime
    fun initReminderIfNeeded()
    fun reschedule(newTime: RemindTime)

}

class SchedulerRepositoryImpl: SchedulerRepository, KoinComponent {

    private val preferences: SharedPreferences by inject()
    private val scheduler: RemindScheduler by inject()

    override fun currentRemindTime(): RemindTime {
        return RemindTime(
            hour = preferences.getInt("remindHourOfDay", 9),
            minute = preferences.getInt("remindMinuteOfHour", 0)
        )
    }

    override fun initReminderIfNeeded() {
        scheduler.startIfNotSet(currentRemindTime())
    }

    override fun reschedule(newTime: RemindTime) {
        scheduler.reschedule(newTime)
        preferences.edit()
            .putInt("remindHourOfDay", newTime.hour)
            .putInt("remindMinuteOfHour", newTime.minute)
            .commit()
    }

}
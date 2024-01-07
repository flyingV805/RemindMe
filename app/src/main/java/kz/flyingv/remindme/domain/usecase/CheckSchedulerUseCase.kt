package kz.flyingv.remindme.domain.usecase

import android.content.SharedPreferences
import kz.flyingv.remindme.data.preferences.PreferencesKeys
import kz.flyingv.remindme.data.scheduler.RemindScheduler
import kz.flyingv.remindme.domain.entity.ReminderTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CheckSchedulerUseCase: KoinComponent {

    private val preferences: SharedPreferences by inject()
    private val scheduler: RemindScheduler by inject()

    operator fun invoke(){
        val selectedHour = preferences.getInt(PreferencesKeys.remindHour, PreferencesKeys.defaultRemindHour)
        val remindMinute = preferences.getInt(PreferencesKeys.remindMinute, PreferencesKeys.defaultRemindMinute)
        scheduler.startIfNotSet(ReminderTime(selectedHour, remindMinute))
    }

}
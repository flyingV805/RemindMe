package kz.flyingv.remindme.data.scheduler

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kz.flyingv.remindme.data.repository.SchedulerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmPermissionReceiver: BroadcastReceiver(), KoinComponent {

    private val schedulerRepository: SchedulerRepository by inject()

    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent?.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
            schedulerRepository.initReminderIfNeeded()
        }
    }

}
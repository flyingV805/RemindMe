package kz.flyingv.remindme.data.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kz.flyingv.remindme.data.repository.SchedulerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {


    private val schedulerRepository: SchedulerRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            //RemindersManager.startReminder(context)
            schedulerRepository.initReminderIfNeeded()
        }
    }
}
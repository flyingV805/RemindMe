package kz.flyingv.remindme.data.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.repository.SchedulerRepository
import kz.flyingv.remindme.features.notificator.NotificatorNew
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SchedulerReceiver : BroadcastReceiver(), KoinComponent {

    private val schedulerRepository: SchedulerRepository by inject()

    private val job = SupervisorJob()
    private val receiverScope = CoroutineScope(Dispatchers.IO + job)

    override fun onReceive(context: Context, intent: Intent) {
        receiverScope.launch {
            NotificatorNew(context).doWork()
            schedulerRepository.initReminderIfNeeded()
            delay(2000)
            onDestroy()
        }

    }

    private fun onDestroy(){
        job.cancel()
    }

}
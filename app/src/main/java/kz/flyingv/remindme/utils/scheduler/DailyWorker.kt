package kz.flyingv.remindme.utils.scheduler

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.utils.notifications.Notificator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.TimeUnit


class DailyWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    override fun doWork(): Result {

        val notificator = Notificator(context = context)
        val currentDate = Calendar.getInstance()

        reminderRepository.getWorkerReminders().forEach { reminder ->
            notificator.showNotification(reminder)
            val type = reminder.type
            when(type){
                is RemindType.Daily -> {
                    //remind anyway
                }
                is RemindType.Monthly -> {
                    //remind if day of month is right
                    if(currentDate.get(Calendar.DAY_OF_MONTH) == type.dayOfMonth){

                    }
                }
                is RemindType.Weekly -> {
                    //remind if day of week is right
                    if(currentDate.get(Calendar.DAY_OF_WEEK) == type.dayOfWeek){

                    }
                }
                is RemindType.Yearly -> {
                    //remind if day of year is right
                    if(currentDate.get(Calendar.DAY_OF_YEAR) == type.dayOfMonth){

                    }
                }
            }
        }

        reschedule()
        return Result.success()
    }

    private fun reschedule(){
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, 10)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {dueDate.add(Calendar.HOUR_OF_DAY, 24)}

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(RemindScheduler.reminderTag)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(dailyWorkRequest)
    }

}
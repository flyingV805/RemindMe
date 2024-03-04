package kz.flyingv.remindme.data.scheduler

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.work.WorkInfo
import kz.flyingv.remindme.domain.entity.ReminderTime
import java.util.concurrent.ExecutionException


class RemindSchedulerImpl(private val context: Context): RemindScheduler {

    override fun isPermissionsAvailable(): Boolean = true

    override fun startIfNotSet(time: ReminderTime){

        val statuses = WorkManager.getInstance(context).getWorkInfosByTag(reminderTag)
        var running = false
        try {
            val workInfoList: List<WorkInfo> = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        if(!running){
            start(time.hour, time.minute)
        }

    }

    private fun start(hourOfDay: Int, minuteOfHour: Int){
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag)
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dueDate.set(Calendar.MINUTE, minuteOfHour)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {dueDate.add(Calendar.HOUR_OF_DAY, 24)}

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
/*
        val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificatorWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(reminderTag)
            .build()

        WorkManager.getInstance(context).enqueue(dailyWorkRequest)*/
    }

    override fun reschedule(time: ReminderTime){
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag)
        start(time.hour, time.minute)
    }

    companion object {

        const val reminderTag = "RemindSchedulerWork"

    }

}

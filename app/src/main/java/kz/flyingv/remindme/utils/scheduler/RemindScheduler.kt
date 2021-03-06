package kz.flyingv.remindme.utils.scheduler

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.work.WorkInfo
import kz.flyingv.remindme.data.model.RemindTime
import java.util.concurrent.ExecutionException


class RemindScheduler(private val context: Context) {

    fun startIfNotSet(time: RemindTime){
        val statuses = WorkManager.getInstance(context).getWorkInfosByTag("RemindSchedulerWork")
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

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(reminderTag)
            .build()

        WorkManager.getInstance(context).enqueue(dailyWorkRequest)
    }

    fun reschedule(time: RemindTime){
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag)
        start(time.hour, time.minute)
    }

    companion object {

        const val reminderTag = "RemindSchedulerWork"

    }

}

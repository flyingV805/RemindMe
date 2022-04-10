package kz.flyingv.remindme.scheduler

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class RemindScheduler(private val context: Context) {

    fun startIfNotSet(hourOfDay: Int){
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag)
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {dueDate.add(Calendar.HOUR_OF_DAY, 24)}

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag(reminderTag)
            .build()

        WorkManager.getInstance(context).enqueue(dailyWorkRequest)
    }

    fun reschedule(newHourOfDay: Int){
        WorkManager.getInstance(context).cancelAllWorkByTag(reminderTag)
        startIfNotSet(newHourOfDay)
    }

    companion object {

        const val reminderTag = "RemindSchedulerWork"

    }

}

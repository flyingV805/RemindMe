package kz.flyingv.remindme.utils.scheduler

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.data.repository.SchedulerRepository
import kz.flyingv.remindme.utils.datetime.DatetimeUtils
import kz.flyingv.remindme.utils.notifications.Notificator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.TimeUnit


class DailyWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val schedulerRepository: SchedulerRepository by inject()

    override fun doWork(): Result {

        val notificator = Notificator(context = context)
        val currentDate = Calendar.getInstance()

        reminderRepository.getWorkerReminders().forEach { reminder ->
            when(val type = reminder.type){
                is RemindType.Daily -> {
                    notificator.showNotification(reminder)
                }
                is RemindType.Weekly -> {
                    if(DatetimeUtils.dayOfWeekIndex(currentDate) == type.dayOfWeek){
                        notificator.showNotification(reminder)
                    }
                }
                is RemindType.Monthly -> {
                    val reminderDayInMonth = (type.dayOfMonth + 1)
                    val currentDayInMonth = currentDate.get(Calendar.DAY_OF_MONTH)
                    if(currentDayInMonth == reminderDayInMonth){
                        notificator.showNotification(reminder)
                    }
                    //extra case, month don't have much days
                    val currentMonth = DatetimeUtils.currentMonth(currentDate)
                    val daysInCurrentMonth = DatetimeUtils.daysInMonth(currentMonth) //currentDate.get(Calendar.MONTH)
                    if(reminderDayInMonth > daysInCurrentMonth && currentDayInMonth == daysInCurrentMonth){
                        notificator.showNotification(reminder)
                    }
                }
                is RemindType.Yearly -> {
                    val reminderDayInMonth = (type.dayOfMonth + 1)
                    val currentDayInMonth = currentDate.get(Calendar.DAY_OF_MONTH)

                    val reminderMonth = (type.month)
                    val currentMonth = DatetimeUtils.currentMonth(currentDate)

                    Log.d("Y reminderDayInMonth", reminderDayInMonth.toString())
                    Log.d("Y currentDayInMonth", currentDayInMonth.toString())
                    Log.d("Y reminderMonth", reminderMonth.toString())
                    Log.d("Y currentMonth", currentMonth.toString())

                    if(reminderDayInMonth == currentDayInMonth && reminderMonth == currentMonth){
                        notificator.showNotification(reminder)
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

        val remindTime = schedulerRepository.currentRemindTime()

        dueDate.set(Calendar.HOUR_OF_DAY, remindTime.hour)
        dueDate.set(Calendar.MINUTE, remindTime.minute)
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
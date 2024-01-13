package kz.flyingv.remindme.features.notificator

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.domain.usecase.GetWorkerRemindersUseCase
import kz.flyingv.remindme.domain.usecase.UpdateLastShownUseCase
import kz.flyingv.remindme.domain.usecase.UpdateSchedulerUseCase
import kz.flyingv.remindme.utils.datetime.DatetimeUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class NotificatorDubg(val context: Context): KoinComponent {

    private val getWorkerRemindersUseCase: GetWorkerRemindersUseCase by inject()
    private val updateSchedulerUseCase: UpdateSchedulerUseCase by inject()
    private val updateLastShownUseCase: UpdateLastShownUseCase by inject()


    fun doWork(): ListenableWorker.Result {

        Log.w("NotificatorWorker", "NotificatorWorker is started")

        val notificator = Notificator(context = context)
        val currentDate = Calendar.getInstance()

        getWorkerRemindersUseCase().forEach { reminder ->
            when(val type = reminder.type){
                is ReminderType.Daily -> {
                    updateLastShownUseCase(reminder, System.currentTimeMillis())
                    notificator.showNotification(reminder)
                }
                is ReminderType.Weekly -> {
                    if(DatetimeUtils.dayOfWeekIndex(currentDate) == type.dayOfWeek){
                        updateLastShownUseCase(reminder, System.currentTimeMillis())
                        notificator.showNotification(reminder)
                    }
                }
                is ReminderType.Monthly -> {
                    val reminderDayInMonth = type.dayOfMonth
                    val currentDayInMonth = currentDate.get(Calendar.DAY_OF_MONTH)
                    if(currentDayInMonth == reminderDayInMonth){
                        updateLastShownUseCase(reminder, System.currentTimeMillis())
                        notificator.showNotification(reminder)
                    }
                    //extra case, month don't have much days
                    val currentMonth = DatetimeUtils.currentMonth(currentDate)
                    val daysInCurrentMonth = DatetimeUtils.daysInMonth(currentMonth) //currentDate.get(Calendar.MONTH)
                    if(reminderDayInMonth > daysInCurrentMonth && currentDayInMonth == daysInCurrentMonth){
                        updateLastShownUseCase(reminder, System.currentTimeMillis())
                        notificator.showNotification(reminder)
                    }
                }
                is ReminderType.Yearly -> {
                    val reminderDayInMonth = type.dayOfMonth
                    val currentDayInMonth = currentDate.get(Calendar.DAY_OF_MONTH)

                    val reminderMonth = (type.month)
                    val currentMonth = DatetimeUtils.currentMonth(currentDate)

                    if(reminderDayInMonth == currentDayInMonth && reminderMonth == currentMonth){
                        updateLastShownUseCase(reminder, System.currentTimeMillis())
                        notificator.showNotification(reminder)
                    }
                }
            }

        }

        updateSchedulerUseCase()

        return ListenableWorker.Result.success()
    }


}
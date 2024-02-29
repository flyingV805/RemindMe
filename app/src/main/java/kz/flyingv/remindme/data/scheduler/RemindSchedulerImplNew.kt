package kz.flyingv.remindme.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import kz.flyingv.remindme.domain.entity.ReminderTime
import java.util.Calendar

class RemindSchedulerImplNew(private val context: Context): RemindScheduler {

    override fun isPermissionsAvailable(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S)
            alarmManager.canScheduleExactAlarms()
        else
            true
    }

    override fun startIfNotSet(time: ReminderTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Log.w("alarmManager", alarmManager.nextAlarmClock.triggerTime.toString())
        //Log.w("alarmManager", isAlarmSet().toString())
        val intent = schedulerIntent()
        alarmManager.cancel(intent)
        start(time.hour, time.minute)

    }

    private fun start(hourOfDay: Int, minuteOfHour: Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = schedulerIntent()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dueDate.set(Calendar.MINUTE, minuteOfHour)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {dueDate.add(Calendar.HOUR_OF_DAY, 24)}

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueDate.timeInMillis, intent)
        }catch (se: SecurityException){
            se.printStackTrace()
        }

    }

    override fun reschedule(time: ReminderTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = schedulerIntent()
        alarmManager.cancel(intent)
        start(time.hour, time.minute)
    }

    private fun schedulerIntent(): PendingIntent {
        return Intent(context, SchedulerReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderTag.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }

    private fun isAlarmSet(): Boolean {
        val intent = Intent(context, SchedulerReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            reminderTag.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null
    }

    companion object {

        const val reminderTag = "RemindSchedulerWork"

    }

}
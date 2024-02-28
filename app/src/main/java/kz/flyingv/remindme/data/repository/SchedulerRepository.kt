package kz.flyingv.remindme.data.repository

import kz.flyingv.remindme.domain.entity.ReminderTime


interface SchedulerRepository {

    fun isSchedulerPermissionsAvailable(): Boolean
    fun currentRemindTime(): ReminderTime
    fun initReminderIfNeeded()
    fun reschedule(newTime: ReminderTime)
    fun updateScheduler()

}
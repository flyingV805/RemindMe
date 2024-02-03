package kz.flyingv.remindme.data.scheduler

import kz.flyingv.remindme.domain.entity.ReminderTime

interface RemindScheduler {

    fun startIfNotSet(time: ReminderTime)

    fun reschedule(time: ReminderTime)

}
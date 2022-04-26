package kz.flyingv.remindme.data.repository

import kz.flyingv.remindme.utils.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface SchedulerRepository {

    fun initReminderIfNeeded()
    fun reschedule(newHourOfDay: Int)

}

class SchedulerRepositoryImpl: SchedulerRepository, KoinComponent {

    private val scheduler: RemindScheduler by inject()

    override fun initReminderIfNeeded() {
        scheduler.startIfNotSet()
    }

    override fun reschedule(newHourOfDay: Int) {
        scheduler.reschedule(newHourOfDay)
    }

}
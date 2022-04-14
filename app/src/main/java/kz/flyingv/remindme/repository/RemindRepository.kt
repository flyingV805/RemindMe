package kz.flyingv.remindme.repository

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.room.Database
import kz.flyingv.remindme.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ReminderRepository {

    fun initReminderIfNeeded()
    fun addNewRemind(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>

}

class ReminderRepositoryImpl: ReminderRepository, KoinComponent {

    private val database: Database by inject()
    private val scheduler: RemindScheduler by inject()

    override fun initReminderIfNeeded() {
        scheduler.startIfNotSet()
    }

    override fun addNewRemind(reminder: Reminder) {

        database.reminderDao().insert(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return database.reminderDao().getAll()
    }
}
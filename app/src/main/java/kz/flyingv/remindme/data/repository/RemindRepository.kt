package kz.flyingv.remindme.data.repository

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.data.datastore.Database
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ReminderRepository {

    fun addNewRemind(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>
    fun getWorkerReminders(): List<Reminder>
    fun deleteRemind(reminder: Reminder)

}

class ReminderRepositoryImpl: ReminderRepository, KoinComponent {

    private val database: Database by inject()

    override fun addNewRemind(reminder: Reminder) {

        database.reminderDao().insert(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return database.reminderDao().getAllFlow()
    }

    override fun getWorkerReminders(): List<Reminder> {
        return database.reminderDao().getAll()
    }

    override fun deleteRemind(reminder: Reminder) {
        database.reminderDao().delete(reminder)
    }

}
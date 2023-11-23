package kz.flyingv.remindme.data.repository

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.data.datastore.mapper.ReminderActionMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderIconMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderTypeMapper
import kz.flyingv.remindme.data.datastore.model.ReminderDTO
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderRepositoryImpl: ReminderRepository, KoinComponent {

    private val database: Database by inject()

    override fun addNewRemind(reminder: Reminder): Boolean {
        return database.reminderDao().insert(
            ReminderDTO(
                name = reminder.name,
                icon = ReminderIconMapper.mapToInt(reminder.icon),
                type = ReminderTypeMapper.mapToString(reminder.type),
                action = ReminderActionMapper.mapToString(reminder.action),
                lastShow = 0
            )
        ) == 1
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        TODO("Not yet implemented")
    }

    override fun getWorkerReminders(): List<Reminder> {
        TODO("Not yet implemented")
    }

    override fun updateLastShow(reminder: Reminder, lastShowMills: Long) {
        database.reminderDao().updateLastShow(reminder.id, lastShowMills)
    }

    override fun deleteRemind(reminder: Reminder) {
        TODO("Not yet implemented")
    }

}
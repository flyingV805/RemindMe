package kz.flyingv.remindme.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.data.datastore.mapper.ReminderActionMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderIconMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderTypeMapper
import kz.flyingv.remindme.data.datastore.model.ReminderDTO
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderRepositoryImpl: ReminderRepository, KoinComponent {

    private val database: Database by inject()

    override suspend fun addNewRemind(reminder: Reminder): Boolean {
        return database.reminderDao().insert(
            ReminderDTO(
                name = reminder.name,
                icon = ReminderIconMapper.mapToInt(reminder.icon),
                type = ReminderTypeMapper.mapToString(reminder.type),
                action = ReminderActionMapper.mapToString(reminder.action),
                lastShow = 0
            )
        ) == 1L
    }

    override suspend fun addRemoteReminds(reminders: List<Reminder>): Boolean {
        return database.reminderDao().insert(
            reminders.map { reminder ->
                ReminderDTO(
                    name = reminder.name,
                    icon = ReminderIconMapper.mapToInt(reminder.icon),
                    type = ReminderTypeMapper.mapToString(reminder.type),
                    action = ReminderActionMapper.mapToString(reminder.action),
                    lastShow = 0
                )
            }

        ).size > 0L
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        TODO("Not yet implemented")
    }

    override fun getAllRemindersPaged(): Flow<PagingData<Reminder>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = { database.reminderDao().getAllPaged() }
        ).flow.map { pagingData ->
            pagingData.map { reminderDto ->
                Reminder(
                    id = reminderDto.id,
                    name = reminderDto.name,
                    icon = ReminderIconMapper.mapFromInt(reminderDto.icon),
                    type = ReminderTypeMapper.mapFromString(reminderDto.type) ?: ReminderType.Daily,
                    action = ReminderActionMapper.mapFromString(reminderDto.action) ?: ReminderAction.DoNothing,
                    lastShow = reminderDto.lastShow
                )
            }
        }
    }

    override fun getWorkerReminders(): List<Reminder> {
        return database.reminderDao().getAll().map { reminderDto ->
            Reminder(
                id = reminderDto.id,
                name = reminderDto.name,
                icon = ReminderIconMapper.mapFromInt(reminderDto.icon),
                type = ReminderTypeMapper.mapFromString(reminderDto.type) ?: ReminderType.Daily,
                action = ReminderActionMapper.mapFromString(reminderDto.action) ?: ReminderAction.DoNothing,
                lastShow = reminderDto.lastShow
            )
        }
    }

    override fun updateLastShow(reminder: Reminder, lastShowMills: Long) {
        database.reminderDao().updateLastShow(reminder.id, lastShowMills)
    }

    override suspend fun searchReminders(search: String): List<Reminder> {
        return database.reminderDao().search("%$search%").map { reminderDto ->
            Reminder(
                id = reminderDto.id,
                name = reminderDto.name,
                icon = ReminderIconMapper.mapFromInt(reminderDto.icon),
                type = ReminderTypeMapper.mapFromString(reminderDto.type) ?: ReminderType.Daily,
                action = ReminderActionMapper.mapFromString(reminderDto.action) ?: ReminderAction.DoNothing,
                lastShow = reminderDto.lastShow
            )
        }
    }

    override suspend fun deleteRemind(reminder: Reminder) {
        database.reminderDao().delete(reminder.id)
    }

}
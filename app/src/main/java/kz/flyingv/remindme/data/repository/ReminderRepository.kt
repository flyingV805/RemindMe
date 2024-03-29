package kz.flyingv.remindme.data.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.domain.entity.Reminder

interface ReminderRepository {
    suspend fun addNewRemind(reminder: Reminder): Boolean
    suspend fun addRemoteReminds(reminders: List<Reminder>): Boolean
    fun getAllReminders(): Flow<List<Reminder>>
    fun getAllRemindersPaged(): Flow<PagingData<Reminder>>
    fun getWorkerReminders(): List<Reminder>
    fun updateLastShow(reminder: Reminder, lastShowMills: Long)
    suspend fun searchReminders(search: String): List<Reminder>
    suspend fun deleteRemind(reminder: Reminder)
}
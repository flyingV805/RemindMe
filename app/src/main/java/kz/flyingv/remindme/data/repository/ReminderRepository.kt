package kz.flyingv.remindme.data.repository

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.domain.entity.Reminder

interface ReminderRepository {
    fun addNewRemind(reminder: Reminder): Boolean
    fun getAllReminders(): Flow<List<Reminder>>
    fun getWorkerReminders(): List<Reminder>
    fun updateLastShow(reminder: Reminder, lastShowMills: Long)
    fun deleteRemind(reminder: Reminder)
}
package kz.flyingv.remindme.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.data.datastore.Database
import kz.flyingv.remindme.utils.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ReminderRepository {

    fun initReminderIfNeeded()
    fun addNewRemind(reminder: Reminder)
    fun getAllReminders(): Flow<List<Reminder>>
    fun getInstalledAppsForAction(): List<Any?>

}

class ReminderRepositoryImpl: ReminderRepository, KoinComponent {

    private val context: Context by inject()
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

    override fun getInstalledAppsForAction(): List<ApplicationInfo> {
        return context.packageManager.getInstalledApplications(0)
    }

}
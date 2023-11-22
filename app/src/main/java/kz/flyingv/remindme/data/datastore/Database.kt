package kz.flyingv.remindme.data.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.flyingv.remindme.data.datastore.model.ReminderDTO

@Database(entities = [ReminderDTO::class], version = 1)
abstract class Database: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}
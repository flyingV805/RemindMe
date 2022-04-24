package kz.flyingv.remindme.data.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.data.datastore.converter.RemindActionConverter
import kz.flyingv.remindme.data.datastore.converter.RemindIconConverter
import kz.flyingv.remindme.data.datastore.converter.RemindPriorityConverter
import kz.flyingv.remindme.data.datastore.converter.RemindTypeConverter

@Database(entities = [Reminder::class], version = 1)
@TypeConverters(RemindPriorityConverter::class, RemindTypeConverter::class, RemindIconConverter::class, RemindActionConverter::class)
abstract class Database: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}
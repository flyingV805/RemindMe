package kz.flyingv.remindme.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.room.converter.RemindActionConverter
import kz.flyingv.remindme.room.converter.RemindPriorityConverter
import kz.flyingv.remindme.room.converter.RemindTypeConverter

@Database(entities = [Reminder::class], version = 1)
@TypeConverters(RemindPriorityConverter::class, RemindTypeConverter::class, RemindActionConverter::class)
abstract class Database: RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}
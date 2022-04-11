package kz.flyingv.remindme.room

import androidx.room.Dao
import androidx.room.Query
import kz.flyingv.remindme.model.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    fun getAll(): List<Reminder>

}
package kz.flyingv.remindme.data.datastore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.model.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    fun getAll(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reminder: Reminder)

}
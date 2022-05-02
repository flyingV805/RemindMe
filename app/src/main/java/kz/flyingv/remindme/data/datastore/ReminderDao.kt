package kz.flyingv.remindme.data.datastore

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.model.Reminder

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    fun getAllFlow(): Flow<List<Reminder>>

    @Query("SELECT * FROM Reminder")
    fun getAll(): List<Reminder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reminder: Reminder)

    @Query("UPDATE Reminder SET lastShow = :lastShowMills WHERE id = :id")
    fun updateLastShow(id: Int, lastShowMills: Long)

    @Delete
    fun delete(reminder: Reminder)

}
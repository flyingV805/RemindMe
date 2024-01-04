package kz.flyingv.remindme.data.datastore

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.datastore.model.ReminderDTO

@Dao
interface ReminderDao {

    @Query("SELECT * FROM ReminderDTO")
    fun getAllFlow(): Flow<List<ReminderDTO>>

    @Query("SELECT * FROM ReminderDTO")
    fun getAllPaged(): PagingSource<Int, ReminderDTO>

    @Query("SELECT * FROM ReminderDTO")
    fun getAll(): List<ReminderDTO>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(reminder: ReminderDTO): Long

    @Query("UPDATE ReminderDTO SET lastShow = :lastShowMills WHERE id = :id")
    fun updateLastShow(id: Int, lastShowMills: Long)

    @Delete
    fun delete(reminder: ReminderDTO)

}
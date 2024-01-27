package kz.flyingv.remindme.data.datastore.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class ReminderDTO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: Int,
    val type: String,
    val action: String?,
    val lastShow: Long
)
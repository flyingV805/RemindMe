package kz.flyingv.remindme.data.datastore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReminderDTO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: Int,
    val type: String,
    val action: String?,
    val lastShow: Long
)
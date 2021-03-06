package kz.flyingv.remindme.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: RemindIcon,
    val type: RemindType,
    val action: RemindAction?,
    val lastShow: Long
)
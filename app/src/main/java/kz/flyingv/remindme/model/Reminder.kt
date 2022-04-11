package kz.flyingv.remindme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String?,
    val icon: Int?,
    val avatarUri: String?,
    val priority: RemindPriority,
    val type: RemindType,
    val action: RemindAction?,
    val time: Int,
    val lastShow: Long
)
package kz.flyingv.remindme.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: Int?,
    val type: RemindType,
    val action: RemindAction?,
    val lastShow: Long
)
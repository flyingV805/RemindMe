package kz.flyingv.remindme.data.datastore.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InstalledAppDTO(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: ByteArray?,
    val launchActivity: String
)

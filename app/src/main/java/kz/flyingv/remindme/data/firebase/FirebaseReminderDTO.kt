package kz.flyingv.remindme.data.firebase

import com.google.android.gms.common.annotation.KeepName
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseReminderDTO(
    val name: String? = null,
    val icon: Int? = null,
    val type: String? = null,
    val action: String? = null,
    val lastShow: Long? = null,
    val markAsDeleted: Boolean? = null
)
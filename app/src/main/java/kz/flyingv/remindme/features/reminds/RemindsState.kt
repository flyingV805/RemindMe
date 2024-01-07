package kz.flyingv.remindme.features.reminds

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.Reminder

data class RemindsState(
    val updating: Boolean = false,
    val searching: Boolean = false,
    val searchString: String = "",
    val showNewReminder: Boolean = false,
    val reminderForDelete: Reminder? = null,
    val reminds: Flow<PagingData<Reminder>>? = null,
    val authorized: Boolean = true,
    val avatarUrl: String = "https://static.wikia.nocookie.net/warner-bros-entertainment/images/e/e5/Courage_cartoon_network.png/revision/latest/thumbnail/width/360/height/450?cb=20170930042909",
    val sync: Boolean = true
): UIState
package kz.flyingv.remindme.features.reminds

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.Reminder

data class RemindsState(
    val updating: Boolean = false,
    val searching: Boolean = false,
    val showNewReminder: Boolean = false,
    val reminds: Flow<PagingData<Reminder>>? = null
): UIState
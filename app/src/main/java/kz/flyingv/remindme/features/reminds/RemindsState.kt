package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.Reminder

data class RemindsState(
    val updating: Boolean = false,
    val searching: Boolean = false,
    val reminds: List<Reminder> = emptyList()
): UIState
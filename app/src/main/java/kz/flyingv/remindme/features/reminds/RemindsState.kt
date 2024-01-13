package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.Reminder

data class RemindsState(
    val updating: Boolean = false,
    val searching: Boolean = false,
    val searchString: String = "",
    val showNewReminder: Boolean = false,
    val reminderForDelete: Reminder? = null,
    val showRemindTime: Boolean = false,
    val showPermissionsRequest: Boolean = false,
    val searchReminds: List<Reminder> = emptyList(),
    val authorized: Boolean = false,
    val avatarUrl: String = "",
    val showAuthDialog: Boolean = false,
    // auth and sync
    val showProfileDialog: Boolean = false,
    val syncInProgress: Boolean = false
): UIState
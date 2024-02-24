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
    val askNotificationPermissions: Boolean = false,
    val askAlarmPermissions: Boolean = false,
    val askSchedulePermissions: Boolean = false,
    val searchReminds: List<Reminder> = emptyList(),
    val authorized: Boolean = false,
    val avatarUrl: String = "",
    // auth and sync
    val showProfileDialog: Boolean = false,
    val syncInProgress: Boolean = false
): UIState
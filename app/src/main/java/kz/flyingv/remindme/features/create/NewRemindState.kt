package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType

data class NewRemindState(
    val name: String = "",
    val reminderType: ReminderType = ReminderType.Daily,
    val reminderAction: ReminderAction = ReminderAction.DoNothing,
    val error: ValidationError
): UIState


enum class ValidationError {
    NeedName,
    NeedApp,
    NeedLink
}
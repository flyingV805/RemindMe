package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIAction
import kz.flyingv.remindme.domain.entity.Reminder

sealed class RemindsAction: UIAction {

    data object StartSearch: RemindsAction()
    data class Search(val input: String): RemindsAction()
    data object EndSearch: RemindsAction()
    data object ShowNewReminder: RemindsAction()
    data object HideNewReminder: RemindsAction()
    data object ShowReminderTime: RemindsAction()
    data object HideReminderTime: RemindsAction()
    data class AskForDelete(val reminder: Reminder): RemindsAction()
    data object CancelDelete: RemindsAction()
    data class Delete(val reminder: Reminder): RemindsAction()

}
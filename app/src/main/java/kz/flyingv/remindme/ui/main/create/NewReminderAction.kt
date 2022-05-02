package kz.flyingv.remindme.ui.main.create

import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.data.model.RemindType

sealed class NewReminderAction {
    class UpdateName(val name: String): NewReminderAction()
    class UpdateIcon(val icon: RemindIcon): NewReminderAction()
    class UpdateType(val remindType: RemindType): NewReminderAction()
    class UpdateAction(val remindAction: RemindAction): NewReminderAction()
    object CreateReminder: NewReminderAction()
}
package kz.flyingv.remindme.ui.main.create

import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum

sealed class NewReminderAction {
    class UpdateName(val name: String): NewReminderAction()
    class UpdateIcon(val icon: RemindIcon): NewReminderAction()
    class UpdateType(val remindType: RemindType): NewReminderAction()
    class UpdateAction(val remindAction: RemindActionEnum): NewReminderAction()
    object CreateReminder: NewReminderAction()
}
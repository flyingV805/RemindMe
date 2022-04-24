package kz.flyingv.remindme.ui.main.create

import kz.flyingv.remindme.data.model.RemindIcon


sealed class NewReminderAction {
    class UpdateName(val name: String): NewReminderAction()
    class UpdateIcon(val icon: RemindIcon): NewReminderAction()
    object CreateReminder: NewReminderAction()
}
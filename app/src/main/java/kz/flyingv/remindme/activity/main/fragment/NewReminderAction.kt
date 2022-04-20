package kz.flyingv.remindme.activity.main.fragment

import kz.flyingv.remindme.model.RemindIcon

sealed class NewReminderAction {
    class UpdateName(val name: String): NewReminderAction()
    class UpdateIcon(val icon: RemindIcon): NewReminderAction()
    object CreateReminder: NewReminderAction()
}
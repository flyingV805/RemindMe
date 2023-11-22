package kz.flyingv.remindme.domain.entity

sealed class ReminderAction {
    data object DoNothing: ReminderAction()
    class OpenApp(val installedApp: InstalledApp?): ReminderAction()
    class OpenUrl(val url: String?): ReminderAction()
}
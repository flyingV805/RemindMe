package kz.flyingv.remindme.data.model

sealed class RemindAction {
    object DoNothing: RemindAction()
    class OpenApp(val installedApp: InstalledApp?): RemindAction()
    class OpenUrl(val url: String?): RemindAction()
}
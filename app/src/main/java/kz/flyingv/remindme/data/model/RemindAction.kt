package kz.flyingv.remindme.data.model

sealed class RemindAction {
    object DoNothing: RemindAction()
    class OpenApp(val appName: String, val appPackage: String): RemindAction()
    class OpenUrl(val url: String): RemindAction()
}
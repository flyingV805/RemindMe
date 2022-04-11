package kz.flyingv.remindme.model

sealed class RemindAction {
    class OpenApp(val appName: String, val appPackage: String): RemindAction()
    class OpenUrl(val url: String): RemindAction()
}
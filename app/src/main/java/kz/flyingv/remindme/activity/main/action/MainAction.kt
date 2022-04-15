package kz.flyingv.remindme.activity.main.action

sealed class MainAction {

    object StartSearch: MainAction()
    class UpdateSearch(val text: String): MainAction()
    object EndSearch: MainAction()
    class CreateReminder(val reminderName: String): MainAction()
}

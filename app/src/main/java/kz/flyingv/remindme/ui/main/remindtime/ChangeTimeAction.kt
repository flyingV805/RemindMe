package kz.flyingv.remindme.ui.main.remindtime

sealed class ChangeTimeAction {

    object GetTime : ChangeTimeAction()
    class UpdateTime(val hour: Int, val minute: Int): ChangeTimeAction()
    object SaveTime : ChangeTimeAction()

}
package kz.flyingv.remindme.features.remindtime

import kz.flyingv.cleanmvi.UIAction

sealed class RemindTimeAction: UIAction {

    data class UpdateTime(val hour: Int, val minute: Int) : RemindTimeAction()
    data object HideDialog: RemindTimeAction()
    data object Hidden: RemindTimeAction()

}
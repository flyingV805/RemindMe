package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIAction
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType

sealed class NewRemindAction: UIAction {

    data class UpdateName(val name: String): NewRemindAction()
    data class UpdateType(val type: ReminderType)
    data class UpdateAction(val action: ReminderAction)
    data object Create: NewRemindAction()

}

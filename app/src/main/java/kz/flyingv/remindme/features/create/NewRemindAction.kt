package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIAction
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.RemindType

sealed class NewRemindAction: UIAction {

    data class UpdateName(val name: String): NewRemindAction()
    data class UpdateIcon(val icon: ReminderIcon): NewRemindAction()
    data class UpdateType(val type: RemindType): NewRemindAction()
    data class UpdateAction(val action: RemindAction): NewRemindAction()
    data object Create: NewRemindAction()

}

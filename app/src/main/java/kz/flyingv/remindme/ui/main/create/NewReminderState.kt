package kz.flyingv.remindme.ui.main.create

import kz.flyingv.remindme.data.model.InstalledApp
import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.ui.statemodel.ValidationError

data class NewReminderState(
    val name: String,
    val icon: RemindIcon,
    val type: RemindType,
    val action: RemindAction,
    val actionApps: List<InstalledApp>,
    val error: ValidationError
)
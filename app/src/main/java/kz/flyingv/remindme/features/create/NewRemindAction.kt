package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIAction
import kz.flyingv.remindme.domain.entity.DayOfWeek
import kz.flyingv.remindme.domain.entity.InstalledApp
import kz.flyingv.remindme.domain.entity.MonthOfYear
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.RemindType

sealed class NewRemindAction: UIAction {

    data class UpdateName(val name: String): NewRemindAction()
    data class UpdateIcon(val icon: ReminderIcon): NewRemindAction()

    data class UpdateType(val type: RemindType): NewRemindAction()
    data class UpdateDayOfWeek(val day: DayOfWeek): NewRemindAction()
    data class UpdateDayOfMonth(val day: Int): NewRemindAction()
    data class UpdateDayOfYear(val day: Int, val monthOfYear: MonthOfYear): NewRemindAction()

    data class UpdateAction(val action: RemindAction): NewRemindAction()
    data class UpdateApp(val app: InstalledApp): NewRemindAction()
    data class UpdateLink(val url: String): NewRemindAction()
    data object Hidden: NewRemindAction()
    data object Create: NewRemindAction()

}

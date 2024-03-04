package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.domain.entity.DayOfWeek
import kz.flyingv.remindme.domain.entity.InstalledApp
import kz.flyingv.remindme.domain.entity.MonthOfYear
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.RemindType
import kz.flyingv.remindme.features.create.uidata.ValidationError

data class NewRemindState(
    val name: String = "",

    val icon: ReminderIcon = ReminderIcon.Cake,

    val type: RemindType = RemindType.Daily,
    //daily
    val daysOfWeek: Set<DayOfWeek> = emptySet(),
    //monthly
    val daysOfMonth: Set<Int> = emptySet(),
    //yearly
    val dayOfYear: Int = 1,
    val monthOfYear: MonthOfYear = MonthOfYear.Jan,
    //action
    val action: RemindAction = RemindAction.Nothing,
    //action app
    val actionApp: InstalledApp? = null,
    val availableApps: List<InstalledApp> = emptyList(),
    //action url
    val actionUrl: String = "",
    //
    val errors: List<ValidationError> = emptyList(),
    val done: Boolean = false
): UIState





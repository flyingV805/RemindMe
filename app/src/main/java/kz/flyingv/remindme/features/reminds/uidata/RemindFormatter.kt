package kz.flyingv.remindme.features.reminds.uidata

import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

class RemindFormatter {

    companion object{

        fun formatRemindType(remindType: ReminderType): String {
            return when(remindType){
                is ReminderType.Daily -> "Every day"
                is ReminderType.Monthly -> "Every month, ".plus(remindType.dayOfMonth).plus(DatetimeUtils.getDayOfMonthSuffix(remindType.dayOfMonth))
                is ReminderType.Weekly -> "Every week, ".plus(DatetimeUtils.dayOfWeekString(remindType.dayOfWeek))
                is ReminderType.Yearly -> "Every year, ".plus(DatetimeUtils.monthOfYearString(remindType.month))
                    .plus(" ")
                    .plus(remindType.dayOfMonth)
                    .plus(DatetimeUtils.getDayOfMonthSuffix(remindType.dayOfMonth))
            }

        }

        fun formatRemindAction(remindAction: ReminderAction?): String {
            return when(remindAction){
                is ReminderAction.DoNothing -> "Action: Not assigned"
                is ReminderAction.OpenApp -> "Action: Open App - ${remindAction.installedApp?.name}"
                is ReminderAction.OpenUrl -> "Action: Open URL - ${remindAction.url}"
                null -> "Action: Error"
            }
        }

    }

}
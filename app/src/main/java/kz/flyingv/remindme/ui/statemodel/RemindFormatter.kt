package kz.flyingv.remindme.ui.statemodel

import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

class RemindFormatter {

    companion object{

        fun formatRemindType(remindType: RemindType): String {
            return when(remindType){
                is RemindType.Daily -> "Every day"
                is RemindType.Monthly -> "Every month, ${remindType.dayOfMonth + 1}${DatetimeUtils.getDayOfMonthSuffix(remindType.dayOfMonth + 1)}"
                is RemindType.Weekly -> "Every week, ${DatetimeUtils.listOfDaysOfWeek()[remindType.dayOfWeek]}"
                is RemindType.Yearly -> "Every year, ${DatetimeUtils.listOfMonths()[remindType.month]} ${remindType.dayOfMonth + 1}${DatetimeUtils.getDayOfMonthSuffix(remindType.dayOfMonth + 1)}"
            }

        }

        fun formatRemindAction(remindAction: RemindAction?): String {
            return when(remindAction){
                is RemindAction.DoNothing -> "Action: Not assigned"
                is RemindAction.OpenApp -> "Action: Open App - ${remindAction.installedApp?.name}"
                is RemindAction.OpenUrl -> "Action: Open URL - ${remindAction.url}"
                null -> "Action: Error"
            }
        }

    }

}
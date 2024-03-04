package kz.flyingv.remindme.features.reminds.uidata

import android.util.Log
import kz.flyingv.remindme.domain.entity.DayOfWeek
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

class RemindFormatter {

    companion object{

        fun formatRemindType(remindType: ReminderType): String {
            return when(remindType){
                is ReminderType.Daily -> "Every day"
                is ReminderType.Monthly -> "Every month, ".plus(formatDaysInMonth(remindType.daysOfMonth))
                is ReminderType.Weekly -> "Every week, ".plus(formatDaysInWeek(remindType.daysOfWeek))
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

        private fun formatDaysInWeek(days: List<DayOfWeek>): String {
            Log.d("formatDaysInWeek", days.toString())
            return when(days.size){
                1 -> DatetimeUtils.dayOfWeekString(days.first())
                2 -> DatetimeUtils.dayOfWeekString(days[0]).plus(" and ").plus(DatetimeUtils.dayOfWeekString(days[1]))
                else -> {
                    var result = ""
                    for (i in 0 until days.size - 1){
                        result += DatetimeUtils.dayOfWeekString(days[i]).plus(", ")
                    }
                    result = result.dropLast(2)
                    result += " and ".plus(DatetimeUtils.dayOfWeekString(days.last()))
                    result
                }
            }
        }

        private fun formatDaysInMonth(days: List<Int>): String {
            return when(days.size){
                1 -> days.first().toString().plus(DatetimeUtils.getDayOfMonthSuffix(days.first()))
                2 -> days[0].toString().plus(DatetimeUtils.getDayOfMonthSuffix(days[0])).plus(" and ").plus(days[1]).plus(DatetimeUtils.getDayOfMonthSuffix(days[1]))
                else -> {
                    var result = ""
                    for (i in 0 until days.size - 1){
                        result += days[i].toString().plus(DatetimeUtils.getDayOfMonthSuffix(days[i])).plus(", ")
                    }
                    result = result.dropLast(2)
                    result += " and ".plus(days.last()).plus(DatetimeUtils.getDayOfMonthSuffix(days.last()))
                    result
                }
            }
        }

    }

}
package kz.flyingv.remindme.data.datastore.mapper

import kz.flyingv.remindme.domain.entity.DayOfWeek
import kz.flyingv.remindme.domain.entity.MonthOfYear
import kz.flyingv.remindme.domain.entity.ReminderType
import org.json.JSONObject

class ReminderTypeMapper {

    companion object {

        private const val typeDaily = 0
        private const val typeWeekly = 1
        private const val typeMonthly = 2
        private const val typeYearly = 3

        fun mapToString(reminderType: ReminderType): String {
            return when(reminderType){
                is ReminderType.Daily -> { JSONObject().put("type", typeDaily).toString() }
                is ReminderType.Weekly -> { JSONObject().put("type", typeWeekly).put("argument", reminderType.dayOfWeek.ordinal).toString() }
                is ReminderType.Monthly -> { JSONObject().put("type", typeMonthly).put("argument", reminderType.dayOfMonth).toString() }
                is ReminderType.Yearly -> { JSONObject().put("type", typeYearly).put("day", reminderType.dayOfMonth).put("month", reminderType.month.ordinal).toString() }
            }
        }

        fun mapFromString(json: String): ReminderType? {
            return try {
                val jsonObject = JSONObject(json)
                when(jsonObject.optInt("type", -1)){
                    typeDaily -> ReminderType.Daily
                    typeWeekly -> ReminderType.Weekly(DayOfWeek.values()[jsonObject.getInt("argument")])
                    typeMonthly -> ReminderType.Monthly(jsonObject.getInt("argument"))
                    typeYearly -> ReminderType.Yearly(jsonObject.getInt("day"), MonthOfYear.values()[jsonObject.getInt("month")])
                    else -> null
                }
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }

    }

}
package kz.flyingv.remindme.data.datastore.mapper

import kz.flyingv.remindme.domain.entity.DayOfWeek
import kz.flyingv.remindme.domain.entity.MonthOfYear
import kz.flyingv.remindme.domain.entity.ReminderType
import org.json.JSONArray
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
                is ReminderType.Weekly -> {
                    val daysOfWeekArray = JSONArray()
                    reminderType.daysOfWeek.forEach { dayOfWeek -> daysOfWeekArray.put(dayOfWeek.value) }
                    JSONObject().put("type", typeWeekly).put("argument", daysOfWeekArray).toString()
                }
                is ReminderType.Monthly -> {
                    val daysOfMonthArray = JSONArray()
                    reminderType.daysOfMonth.forEach { dayOfMonth -> daysOfMonthArray.put(dayOfMonth) }
                    JSONObject().put("type", typeMonthly).put("argument", daysOfMonthArray).toString()
                }
                is ReminderType.Yearly -> { JSONObject().put("type", typeYearly).put("day", reminderType.dayOfMonth).put("month", reminderType.month.ordinal).toString() }
            }
        }

        fun mapFromString(json: String): ReminderType? {
            return try {
                val jsonObject = JSONObject(json)
                when(jsonObject.optInt("type", -1)){
                    typeDaily -> ReminderType.Daily
                    typeWeekly -> {
                        val daysOfWeek = ArrayList<DayOfWeek>()
                        val daysOfWeekArray = jsonObject.optJSONArray("argument")
                        for( i in 0 until (daysOfWeekArray?.length() ?: 0) ){
                            val dayOfWeek = daysOfWeekArray?.optInt(i) ?: 0
                            daysOfWeek.add( DayOfWeek.entries.first { it.value == dayOfWeek } )
                        }
                        ReminderType.Weekly(daysOfWeek)
                    }
                    typeMonthly -> {
                        val daysOfMonth = ArrayList<Int>()
                        val daysOfMonthArray = jsonObject.optJSONArray("argument")
                        for( i in 0 until (daysOfMonthArray?.length() ?: 0) ){
                            val dayOfMonth = daysOfMonthArray?.optInt(i) ?: 0
                            daysOfMonth.add(dayOfMonth)
                        }
                        ReminderType.Monthly(daysOfMonth)
                    }
                    typeYearly -> ReminderType.Yearly(jsonObject.getInt("day"), MonthOfYear.entries[jsonObject.getInt("month")])
                    else -> null
                }
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }

    }

}
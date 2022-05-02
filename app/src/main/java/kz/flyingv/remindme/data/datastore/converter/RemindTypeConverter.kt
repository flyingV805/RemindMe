package kz.flyingv.remindme.data.datastore.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.data.model.DayOfWeek
import kz.flyingv.remindme.data.model.MonthOfYear
import kz.flyingv.remindme.data.model.RemindType
import org.json.JSONObject
import java.lang.Exception

class RemindTypeConverter {

    @TypeConverter
    fun from(remindType: RemindType?): String? {
        return when(remindType){
            is RemindType.Daily -> { JSONObject().put("type", typeDaily).toString() }
            is RemindType.Weekly -> { JSONObject().put("type", typeWeekly).put("argument", remindType.dayOfWeek.ordinal).toString() }
            is RemindType.Monthly -> { JSONObject().put("type", typeMonthly).put("argument", remindType.dayOfMonth).toString() }
            is RemindType.Yearly -> { JSONObject().put("type", typeYearly).put("day", remindType.dayOfMonth).put("month", remindType.month.ordinal).toString() }
            null -> null
        }
    }

    @TypeConverter
    fun to(data: String): RemindType? {
        return try {
            val jsonObject = JSONObject(data)
            when(jsonObject.optInt("type", -1)){
                0 -> RemindType.Daily
                1 -> RemindType.Weekly(DayOfWeek.values()[jsonObject.getInt("argument")])
                2 -> RemindType.Monthly(jsonObject.getInt("argument"))
                3 -> RemindType.Yearly(jsonObject.getInt("day"), MonthOfYear.values()[jsonObject.getInt("month")])
                else -> null
            }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    companion object {

        const val typeDaily = 0
        const val typeWeekly = 1
        const val typeMonthly = 2
        const val typeYearly = 3

    }

}
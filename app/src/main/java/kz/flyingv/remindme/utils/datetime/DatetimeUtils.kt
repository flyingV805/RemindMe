package kz.flyingv.remindme.utils.datetime

import java.util.*

class DatetimeUtils {

    companion object {

        private val daysOfWeekList = listOf("Mn", "Tu", "We", "Th", "Fr", "Sa", "Su")
        private val monthsList = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        fun listOfMonths() = monthsList

        fun listOfDaysOfWeek() = daysOfWeekList

        fun dayOfWeekIndex(calendar: Calendar): Int{
            return when(calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                Calendar.SUNDAY -> 6
                else -> 0
            }
        }

        fun daysInMonth(month: Int): Int{
            val result = if (month == 4 || month == 6 || month == 9 || month == 11){
                30
            }else if(month == 2){
                28
            }else{
                31
            }
            return result
        }

        fun getDayOfMonthSuffix(dayOfMonth: Int): String{
            return when(dayOfMonth % 20){
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> if(dayOfMonth > 30){"st"}else{"th"}
            }
        }

    }

}
package kz.flyingv.remindme.utils.datetime

import kz.flyingv.remindme.domain.entity.MonthOfYear
import java.util.*

class DatetimeUtils {

    companion object {

        fun lastTimeDisplayed(lastShowMills: Long): String {
            return when((System.currentTimeMillis() - lastShowMills) / 1000){
                in 0..60 -> "Less than a minute ago"
                in 60..600 -> "Minutes ago"
                in 600..3600 -> "Less than an hour ago"
                in 3600..43200 -> "Hours ago"
                in 43200..604800 -> "Last week"
                else -> "Haven't reminded yet"
            }
        }


        fun daysInMonth(month: MonthOfYear): Int{
            val monthIndex = month.ordinal + 1

            return when {
                (monthIndex == 4 || monthIndex == 6 || monthIndex == 9 || monthIndex == 11) -> 30
                (monthIndex == 2) -> 28
                else -> 31
            }

        }

        fun getDayOfMonthSuffix(dayOfMonth: Int): String{
            return when(dayOfMonth % 20){
                1 -> "st"
                2 -> "nd"
                3 -> "rd"
                else -> if(dayOfMonth > 30){"st"}else{"th"}
            }
        }

/*
        fun dayOfWeekString(dayOfWeek: DayOfWeek): String {
            return when(dayOfWeek){
                DayOfWeek.MONDAY -> "Mn"
                DayOfWeek.TUESDAY -> "Tu"
                DayOfWeek.WEDNESDAY -> "We"
                DayOfWeek.THURSDAY -> "Th"
                DayOfWeek.FRIDAY -> "Fr"
                DayOfWeek.SATURDAY -> "Sa"
                DayOfWeek.SUNDAY -> "Su"
            }
        }

        fun monthOfYearString(monthOfYear: MonthOfYear): String {
            return when(monthOfYear){
                MonthOfYear.January -> "January"
                MonthOfYear.February -> "February"
                MonthOfYear.March -> "March"
                MonthOfYear.April -> "April"
                MonthOfYear.May -> "May"
                MonthOfYear.June -> "June"
                MonthOfYear.July -> "July"
                MonthOfYear.August -> "August"
                MonthOfYear.September -> "September"
                MonthOfYear.October -> "October"
                MonthOfYear.November -> "November"
                MonthOfYear.December -> "December"
            }
        }

        fun dayOfWeekIndex(calendar: Calendar): DayOfWeek{
            return when(calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY -> DayOfWeek.MONDAY
                Calendar.TUESDAY -> DayOfWeek.TUESDAY
                Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
                Calendar.THURSDAY -> DayOfWeek.THURSDAY
                Calendar.FRIDAY -> DayOfWeek.FRIDAY
                Calendar.SATURDAY -> DayOfWeek.SATURDAY
                Calendar.SUNDAY -> DayOfWeek.SUNDAY
                else -> DayOfWeek.MONDAY
            }
        }

        fun currentMonth(calendar: Calendar): MonthOfYear {
            return when(calendar.get(Calendar.MONTH)){
                Calendar.JANUARY -> MonthOfYear.January
                Calendar.FEBRUARY -> MonthOfYear.February
                Calendar.MARCH -> MonthOfYear.March
                Calendar.APRIL -> MonthOfYear.April
                Calendar.MAY -> MonthOfYear.May
                Calendar.JUNE -> MonthOfYear.June
                Calendar.JULY -> MonthOfYear.July
                Calendar.AUGUST -> MonthOfYear.August
                Calendar.SEPTEMBER -> MonthOfYear.September
                Calendar.OCTOBER -> MonthOfYear.October
                Calendar.NOVEMBER -> MonthOfYear.November
                Calendar.DECEMBER -> MonthOfYear.December
                else -> MonthOfYear.January
            }
        }
*/

    }

}
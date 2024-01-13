package kz.flyingv.remindme.utils.datetime

import kz.flyingv.remindme.domain.entity.DayOfWeek
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

        fun monthOfYearString(monthOfYear: MonthOfYear): String {
            return when(monthOfYear){
                MonthOfYear.Jan -> "January"
                MonthOfYear.Feb -> "February"
                MonthOfYear.Mar -> "March"
                MonthOfYear.Apr -> "April"
                MonthOfYear.May -> "May"
                MonthOfYear.Jun -> "June"
                MonthOfYear.Jul -> "July"
                MonthOfYear.Aug -> "August"
                MonthOfYear.Sep -> "September"
                MonthOfYear.Oct -> "October"
                MonthOfYear.Nov -> "November"
                MonthOfYear.Dec -> "December"
            }
        }

        fun dayOfWeekString(dayOfWeek: DayOfWeek): String {
            return when(dayOfWeek){
                DayOfWeek.Mon -> "Monday"
                DayOfWeek.Tue -> "Tuesday"
                DayOfWeek.Wed -> "Wednesday"
                DayOfWeek.Thu -> "Thursday"
                DayOfWeek.Fri -> "Friday"
                DayOfWeek.Sat -> "Saturday"
                DayOfWeek.Sun -> "Sunday"
            }
        }




        fun dayOfWeekIndex(calendar: Calendar): DayOfWeek{
            return when(calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY -> DayOfWeek.Mon
                Calendar.TUESDAY -> DayOfWeek.Tue
                Calendar.WEDNESDAY -> DayOfWeek.Wed
                Calendar.THURSDAY -> DayOfWeek.Thu
                Calendar.FRIDAY -> DayOfWeek.Fri
                Calendar.SATURDAY -> DayOfWeek.Sat
                Calendar.SUNDAY -> DayOfWeek.Sun
                else -> DayOfWeek.Mon
            }
        }

        fun currentMonth(calendar: Calendar): MonthOfYear {
            return when(calendar.get(Calendar.MONTH)){
                Calendar.JANUARY -> MonthOfYear.Jan
                Calendar.FEBRUARY -> MonthOfYear.Feb
                Calendar.MARCH -> MonthOfYear.Mar
                Calendar.APRIL -> MonthOfYear.Apr
                Calendar.MAY -> MonthOfYear.May
                Calendar.JUNE -> MonthOfYear.Jun
                Calendar.JULY -> MonthOfYear.Jul
                Calendar.AUGUST -> MonthOfYear.Aug
                Calendar.SEPTEMBER -> MonthOfYear.Sep
                Calendar.OCTOBER -> MonthOfYear.Oct
                Calendar.NOVEMBER -> MonthOfYear.Nov
                Calendar.DECEMBER -> MonthOfYear.Dec
                else -> MonthOfYear.Jan
            }
        }


    }

}
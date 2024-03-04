package kz.flyingv.remindme.domain.entity

sealed class ReminderType {
    data object Daily : ReminderType()
    class Weekly(val daysOfWeek: List<DayOfWeek>): ReminderType()
    class Monthly(val daysOfMonth: List<Int>): ReminderType()
    class Yearly(val dayOfMonth: Int, val month: MonthOfYear): ReminderType()
}
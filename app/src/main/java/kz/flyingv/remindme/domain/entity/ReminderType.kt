package kz.flyingv.remindme.domain.entity

sealed class ReminderType {
    data object Daily : ReminderType()
    class Weekly(val dayOfWeek: DayOfWeek): ReminderType()
    class Monthly(val dayOfMonth: Int): ReminderType()
    class Yearly(val dayOfMonth: Int, val month: MonthOfYear): ReminderType()
}
package kz.flyingv.remindme.data.model

sealed class RemindType {
    object Daily : RemindType()
    class Weekly(val dayOfWeek: DayOfWeek): RemindType()
    class Monthly(val dayOfMonth: Int): RemindType()
    class Yearly(val dayOfMonth: Int, val month: MonthOfYear): RemindType()
}
package kz.flyingv.remindme.model

sealed class RemindType {
    object Daily : RemindType()
    class Weekly(val dayOfWeek: Int): RemindType()
    class Monthly(val dayOfMonth: Int): RemindType()
    class Yearly(val dayOfYear: Int): RemindType()
}
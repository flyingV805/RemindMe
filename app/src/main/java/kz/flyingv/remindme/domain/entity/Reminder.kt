package kz.flyingv.remindme.domain.entity


data class Reminder(
    val id: Int = 0,
    val name: String,
    val icon: ReminderIcon,
    val type: ReminderType,
    val action: ReminderAction,
    val lastShow: Long
)
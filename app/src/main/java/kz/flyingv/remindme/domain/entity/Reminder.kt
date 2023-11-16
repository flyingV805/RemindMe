package kz.flyingv.remindme.domain.entity

import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindType

data class Reminder(
    val id: Int,
    val name: String,
    val icon: ReminderIcon,
    val type: RemindType,
    val action: RemindAction?,
    val lastShow: Long
)
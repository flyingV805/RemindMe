package kz.flyingv.remindme.domain.entity


data class Reminder(
    val id: Long = 0,
    val name: String,
    val icon: ReminderIcon,
    val type: ReminderType,
    val action: ReminderAction,
    val lastShow: Long
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Reminder

        if (name != other.name) return false
        if (icon != other.icon) return false
        if (type.javaClass.name != other.type.javaClass.name) return false
        return action.javaClass.name == other.action.javaClass.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + type.javaClass.hashCode()
        result = 31 * result + action.javaClass.hashCode()
        return result
    }
}
package kz.flyingv.remindme.room.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.model.RemindPriority

class RemindPriorityConverter {

    @TypeConverter
    fun from(priority: RemindPriority?): Int? {
        return priority?.ordinal
    }

    @TypeConverter
    fun to(ordinal: Int): RemindPriority {
        return RemindPriority.values()[ordinal]
    }

}
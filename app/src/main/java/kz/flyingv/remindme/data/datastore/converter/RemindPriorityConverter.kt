package kz.flyingv.remindme.data.datastore.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.data.model.RemindPriority

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
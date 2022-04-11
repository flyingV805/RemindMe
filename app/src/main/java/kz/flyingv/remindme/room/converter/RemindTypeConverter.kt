package kz.flyingv.remindme.room.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.model.RemindType

class RemindTypeConverter {

    @TypeConverter
    fun from(remindType: RemindType?): Int? {
        return remindType?.ordinal
    }

    @TypeConverter
    fun to(ordinal: Int): RemindType {
        return RemindType.values()[ordinal]
    }

}
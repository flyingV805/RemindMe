package kz.flyingv.remindme.room.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.model.RemindIcon

class RemindIconConverter {

    @TypeConverter
    fun from(icon: RemindIcon?): Int? {
        return icon?.ordinal
    }

    @TypeConverter
    fun to(ordinal: Int): RemindIcon {
        return RemindIcon.values()[ordinal]
    }

}
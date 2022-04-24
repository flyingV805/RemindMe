package kz.flyingv.remindme.data.datastore.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.data.model.RemindIcon

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
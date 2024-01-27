package kz.flyingv.remindme.data.datastore.mapper

import kz.flyingv.remindme.domain.entity.ReminderIcon

class ReminderIconMapper {

    companion object {

        fun mapToInt(icon: ReminderIcon): Int {
            return icon.ordinal
        }

        fun mapFromInt(ordinal: Int): ReminderIcon {
            return ReminderIcon.entries[ordinal]
        }


    }

}
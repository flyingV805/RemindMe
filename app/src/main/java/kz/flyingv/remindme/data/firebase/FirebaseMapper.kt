package kz.flyingv.remindme.data.firebase

import kz.flyingv.remindme.data.datastore.mapper.ReminderActionMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderIconMapper
import kz.flyingv.remindme.data.datastore.mapper.ReminderTypeMapper
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderType

class FirebaseMapper {

    companion object {

        fun mapToFirebaseReminder(reminder: Reminder): FirebaseReminderDTO {
            return FirebaseReminderDTO(
                name = reminder.name,
                icon = ReminderIconMapper.mapToInt(reminder.icon),
                type = ReminderTypeMapper.mapToString(reminder.type),
                action = ReminderActionMapper.mapToString(reminder.action),
                lastShow = reminder.lastShow
            )
        }

        fun mapFromFirebaseReminder(reminderDto: FirebaseReminderDTO): Reminder {
            return Reminder(
                id = 0,
                name = reminderDto.name ?: "",
                icon = ReminderIconMapper.mapFromInt(reminderDto.icon ?: 0),
                type = ReminderTypeMapper.mapFromString(reminderDto.type ?: "") ?: ReminderType.Daily,
                action = ReminderActionMapper.mapFromString(reminderDto.action) ?: ReminderAction.DoNothing,
                lastShow = reminderDto.lastShow ?: 0
            )
        }

        fun mapFromFirebaseReminder(reminderDto: HashMap<String, Any>): Reminder {
            return Reminder(
                id = 0,
                name = reminderDto["name"] as? String ?: "",
                icon = ReminderIconMapper.mapFromInt(reminderDto["icon"] as? Int ?: 0),
                type = ReminderTypeMapper.mapFromString(reminderDto["type"] as? String ?: "") ?: ReminderType.Daily,
                action = ReminderActionMapper.mapFromString(reminderDto["action"] as? String) ?: ReminderAction.DoNothing,
                lastShow = reminderDto["lastShow"] as? Long ?: 0
            )
        }

    }

}
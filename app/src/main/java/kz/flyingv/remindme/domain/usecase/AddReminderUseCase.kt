package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class AddReminderUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    operator fun invoke(reminder: Reminder): Boolean {
        Calendar.JANUARY
        return reminderRepository.addNewRemind(reminder)
    }

}
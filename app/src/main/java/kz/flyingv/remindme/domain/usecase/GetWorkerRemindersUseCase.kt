package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetWorkerRemindersUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    operator fun invoke(): List<Reminder> {
        return reminderRepository.getWorkerReminders()
    }

}
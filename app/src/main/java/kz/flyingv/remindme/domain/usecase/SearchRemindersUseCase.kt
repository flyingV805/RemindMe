package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchRemindersUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    suspend operator fun invoke(searchFor: String): List<Reminder>{
        return reminderRepository.searchReminders(searchFor)
    }

}
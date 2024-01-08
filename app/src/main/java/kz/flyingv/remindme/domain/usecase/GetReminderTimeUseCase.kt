package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.SchedulerRepository
import kz.flyingv.remindme.domain.entity.ReminderTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetReminderTimeUseCase: KoinComponent {

    private val schedulerRepository: SchedulerRepository by inject()

    operator fun invoke(): ReminderTime {
        return schedulerRepository.currentRemindTime()
    }

}
package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.SchedulerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CheckSchedulerUseCase: KoinComponent {

    private val schedulerRepository: SchedulerRepository by inject()

    operator fun invoke(){
        schedulerRepository.initReminderIfNeeded()
    }

}
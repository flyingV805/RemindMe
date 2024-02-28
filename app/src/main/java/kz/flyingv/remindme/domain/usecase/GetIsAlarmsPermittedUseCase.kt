package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.SchedulerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetIsAlarmsPermittedUseCase: KoinComponent {

    private val reminderRepository: SchedulerRepository by inject()

    operator fun invoke(): Boolean {
        return reminderRepository.isSchedulerPermissionsAvailable()
    }

}
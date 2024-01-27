package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.FirebaseAuthRepository
import kz.flyingv.remindme.data.repository.FirebaseStoreRepository
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DeleteReminderUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val firebaseAuthRepository: FirebaseAuthRepository by inject()
    private val firebaseStoreRepository: FirebaseStoreRepository by inject()

    suspend operator fun invoke(reminder: Reminder){
        reminderRepository.deleteRemind(reminder)

        if(firebaseAuthRepository.isAuthorized()) {
            firebaseStoreRepository.markAsDeleted(reminder)
        }

    }

}
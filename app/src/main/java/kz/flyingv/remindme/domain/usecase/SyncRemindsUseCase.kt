package kz.flyingv.remindme.domain.usecase

import android.util.Log
import kotlinx.coroutines.delay
import kz.flyingv.remindme.data.repository.FirebaseAuthRepository
import kz.flyingv.remindme.data.repository.FirebaseStoreRepository
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.domain.entity.SyncResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncRemindsUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val firebaseAuthRepository: FirebaseAuthRepository by inject()

    private val firebaseStoreRepository: FirebaseStoreRepository by inject()

    suspend operator fun invoke(): SyncResult {

        if(firebaseAuthRepository.isAuthorized()){

            Log.w("firebaseAuthRepository", "AUTHORIZED" )

            val remoteReminders = firebaseStoreRepository.getAllFromFirebaseStore() ?: emptyList()
            val localReminders = reminderRepository.getWorkerReminders()

            val addToRemote = localReminders.subtract(remoteReminders.toSet())
            val addToLocal = remoteReminders.subtract(localReminders.toSet())

            Log.w("remoteReminders", remoteReminders.joinToString() )
            Log.w("localReminders", localReminders.joinToString() )

            Log.w("addToRemote", addToRemote.joinToString() )
            Log.w("addToLocal", addToLocal.joinToString() )

            reminderRepository.addRemoteReminds(addToLocal.toList())
            firebaseStoreRepository.addToFirebaseStore(addToRemote.toList())

        }

        return SyncResult.Success
    }

}
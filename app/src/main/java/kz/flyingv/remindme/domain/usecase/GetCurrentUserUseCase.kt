package kz.flyingv.remindme.domain.usecase

import kz.flyingv.remindme.data.repository.FirebaseAuthRepository
import kz.flyingv.remindme.domain.entity.AuthUser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCurrentUserUseCase: KoinComponent {

    private val firebaseAuthRepository: FirebaseAuthRepository by inject()

    suspend operator fun invoke(): AuthUser? {
        return firebaseAuthRepository.authorizedUser()
    }

}
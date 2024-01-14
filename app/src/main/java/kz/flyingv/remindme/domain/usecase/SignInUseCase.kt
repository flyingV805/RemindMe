package kz.flyingv.remindme.domain.usecase

import com.google.firebase.auth.AuthCredential
import kz.flyingv.remindme.data.repository.FirebaseAuthRepository
import kz.flyingv.remindme.domain.entity.AuthResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignInUseCase: KoinComponent {

    private val firebaseAuthRepository: FirebaseAuthRepository by inject()

    suspend operator fun invoke(authCredential: AuthCredential): AuthResult {
        return firebaseAuthRepository.signInWithCredentials(authCredential)
    }

}
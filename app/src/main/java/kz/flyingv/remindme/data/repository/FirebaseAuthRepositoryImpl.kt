package kz.flyingv.remindme.data.repository

import com.google.firebase.auth.FirebaseAuth
import kz.flyingv.remindme.domain.entity.AuthUser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseAuthRepositoryImpl: FirebaseAuthRepository, KoinComponent {

    private val firebaseAuth: FirebaseAuth by inject()

    override suspend fun authorizedUser(): AuthUser? {
        val currentUser = firebaseAuth.currentUser
        currentUser ?: return null

        return AuthUser(
            userName = currentUser.displayName ?: "",
            avatarUrl = currentUser.photoUrl?.toString() ?: ""
        )
    }



}
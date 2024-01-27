package kz.flyingv.remindme.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kz.flyingv.remindme.domain.entity.AuthResult
import kz.flyingv.remindme.domain.entity.AuthUser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FirebaseAuthRepositoryImpl: FirebaseAuthRepository, KoinComponent {

    private val firebaseAuth: FirebaseAuth by inject()

    override suspend fun isAuthorized(): Boolean {
        val currentUser = firebaseAuth.currentUser
        return currentUser != null
    }

    override suspend fun authorizedUser(): AuthUser? {
        val currentUser = firebaseAuth.currentUser
        currentUser ?: return null

        return AuthUser(
            userName = currentUser.displayName ?: "",
            avatarUrl = currentUser.photoUrl?.toString() ?: ""
        )
    }

    override suspend fun signInWithCredentials(credential: AuthCredential): AuthResult {

        return try {
            firebaseAuth.signInWithCredential(credential).await()
            AuthResult.Success
        }catch (e: Exception){
            AuthResult.Fail(e.message ?: "Unknown error while signing in...")
        }

    }

    override suspend fun signOut(): AuthResult {
        return try {
            firebaseAuth.signOut()
            AuthResult.Success
        }catch (e: Exception){
            AuthResult.Fail(e.message ?: "Unknown error while signing out...")
        }
    }


}
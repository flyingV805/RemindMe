package kz.flyingv.remindme.data.repository

import com.google.firebase.auth.AuthCredential
import kz.flyingv.remindme.domain.entity.AuthResult
import kz.flyingv.remindme.domain.entity.AuthUser

interface FirebaseAuthRepository {

    suspend fun isAuthorized(): Boolean

    suspend fun authorizedUser(): AuthUser?

    suspend fun signInWithCredentials(credential: AuthCredential): AuthResult

    suspend fun signOut(): AuthResult


}
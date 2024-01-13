package kz.flyingv.remindme.data.repository

import kz.flyingv.remindme.domain.entity.AuthUser

interface FirebaseAuthRepository {

    suspend fun authorizedUser(): AuthUser?

}
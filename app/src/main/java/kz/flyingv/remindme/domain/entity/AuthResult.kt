package kz.flyingv.remindme.domain.entity

sealed class AuthResult {

    data object Success: AuthResult()
    class Fail(val message: String): AuthResult()

}
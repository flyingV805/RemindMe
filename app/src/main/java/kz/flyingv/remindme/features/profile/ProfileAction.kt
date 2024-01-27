package kz.flyingv.remindme.features.profile

import com.google.firebase.auth.AuthCredential
import kz.flyingv.cleanmvi.UIAction

sealed class ProfileAction: UIAction {

    data class SignIn(val credentials: AuthCredential): ProfileAction()
    data object SignInFailed: ProfileAction()
    data object SignOut: ProfileAction()

}
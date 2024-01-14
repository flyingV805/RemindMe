package kz.flyingv.remindme.features.profile

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.AuthResult
import kz.flyingv.remindme.domain.usecase.GetCurrentUserUseCase
import kz.flyingv.remindme.domain.usecase.SignInUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel: KoinComponent, UIViewModel<ProfileState, ProfileAction>(
    ProfileState()
) {

    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val signInUseCase: SignInUseCase by inject()

    init {
        viewModelScope.launch {
            val currentUser = getCurrentUserUseCase.invoke()
            pushState(currentState().copy(
                authorized = currentUser != null,
                displayName = currentUser?.userName ?: "",
                avatarUrl = currentUser?.avatarUrl ?: ""
            ))

        }
    }

    override fun reduce(action: ProfileAction) {
        super.reduce(action)

        when(action){
            is ProfileAction.SignIn -> {
                viewModelScope.launch {
                    when(signInUseCase.invoke(action.credentials)){
                        is AuthResult.Fail -> TODO()
                        AuthResult.Success -> {
                            val currentUser = getCurrentUserUseCase.invoke()
                            pushState(currentState().copy(
                                authorized = currentUser != null,
                                displayName = currentUser?.userName ?: "",
                                avatarUrl = currentUser?.avatarUrl ?: ""
                            ))
                        }
                    }
                }

            }
            ProfileAction.SignInFailed -> {

            }
        }

    }

}
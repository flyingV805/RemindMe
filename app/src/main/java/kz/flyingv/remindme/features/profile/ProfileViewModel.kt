package kz.flyingv.remindme.features.profile

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kz.flyingv.cleanmvi.UIViewModel
import org.koin.core.component.KoinComponent

class ProfileViewModel: KoinComponent, UIViewModel<ProfileState, ProfileAction>(
    ProfileState()
) {



    override fun reduce(action: ProfileAction) {
        super.reduce(action)



    }

}
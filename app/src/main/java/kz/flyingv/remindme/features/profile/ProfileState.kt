package kz.flyingv.remindme.features.profile

import kz.flyingv.cleanmvi.UIState

data class ProfileState(
    val authorized: Boolean = false,
    val displayName: String = "",
    val avatarUrl: String = ""
): UIState
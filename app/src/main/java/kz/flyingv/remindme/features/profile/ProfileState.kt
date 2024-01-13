package kz.flyingv.remindme.features.profile

import kz.flyingv.cleanmvi.UIState

data class ProfileState(
    val showInfo: Boolean = true
): UIState
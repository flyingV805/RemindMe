package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIState

data class RemindsState(
    val updating: Boolean = false
): UIState
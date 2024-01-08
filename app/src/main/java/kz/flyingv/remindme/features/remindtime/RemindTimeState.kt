package kz.flyingv.remindme.features.remindtime

import kz.flyingv.cleanmvi.UIState
import kz.flyingv.remindme.data.preferences.PreferencesKeys

data class RemindTimeState(
    val selectHour: Int = PreferencesKeys.defaultRemindHour,
    val selectMinute: Int = PreferencesKeys.defaultRemindMinute,
    val hide: Boolean = false
): UIState

package kz.flyingv.remindme.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import kz.flyingv.remindme.activity.main.state.MainState
import kz.flyingv.remindme.model.RemindType
import kz.flyingv.remindme.model.Reminder

@Composable
fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}

fun previewState(): MainState {
    return MainState(
        listOf(
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
            Reminder(0, ", ", 0,  RemindType.Daily, null, 0),
        ),
        false,
        ""
    )
}
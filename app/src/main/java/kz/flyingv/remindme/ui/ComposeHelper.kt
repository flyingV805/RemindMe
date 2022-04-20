package kz.flyingv.remindme.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import kz.flyingv.remindme.activity.main.MainState
import kz.flyingv.remindme.activity.main.fragment.NewReminderState
import kz.flyingv.remindme.model.RemindIcon
import kz.flyingv.remindme.model.RemindType
import kz.flyingv.remindme.model.Reminder

@Composable
fun isInPreview(): Boolean {
    return LocalInspectionMode.current
}

fun previewMainState(): MainState {
    return MainState(
        listOf(
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
            Reminder(0, ", ", RemindIcon.Cake,  RemindType.Daily, null, 0),
        ),
        false,
        ""
    )
}

fun previewNewReminderState(): NewReminderState {
    return NewReminderState(
        name = "",
        icon = RemindIcon.Cake
    )
}
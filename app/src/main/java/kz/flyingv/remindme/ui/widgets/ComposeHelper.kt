package kz.flyingv.remindme.ui.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.ui.main.MainState
import kz.flyingv.remindme.ui.main.create.NewReminderState
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum
import kz.flyingv.remindme.ui.statemodel.RemindTypeEnum

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
        icon = RemindIcon.Cake,
        type = RemindType.Daily,
        action = RemindActionEnum.Nothing,
        actionApps = emptyList()
    )
}
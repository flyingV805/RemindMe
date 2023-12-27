package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType

class RemindsViewModel: UIViewModel<RemindsState, RemindsAction>(
    RemindsState()
) {

    init {
        pushState(
            currentState().copy(
                reminds = listOf(
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                    Reminder(0, "Reminder", ReminderIcon.Cake, type = ReminderType.Daily, ReminderAction.DoNothing, 0),
                )
            )
        )

    }

    override fun reduce(action: RemindsAction) {
        super.reduce(action)

        when(action){
            RemindsAction.StartSearch -> {
                pushState(currentState().copy(searching = true))
            }
            is RemindsAction.Search -> {

            }
            RemindsAction.EndSearch -> {
                pushState(currentState().copy(searching = false))
            }
        }

    }

}
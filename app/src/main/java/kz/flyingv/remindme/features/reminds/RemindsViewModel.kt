package kz.flyingv.remindme.features.reminds

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.usecase.DeleteReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetRemindersUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemindsViewModel: KoinComponent, UIViewModel<RemindsState, RemindsAction> (
    RemindsState()
) {

    private val remindersUseCase: GetRemindersUseCase by inject()
    private val deleteReminderUseCase: DeleteReminderUseCase by inject()

    val remindersPaged = remindersUseCase().cachedIn(viewModelScope)

    override fun reduce(action: RemindsAction) {
        super.reduce(action)

        when(action){
            RemindsAction.StartSearch -> {
                pushState(currentState().copy(searching = true))
            }
            is RemindsAction.Search -> {
                pushState(currentState().copy(searchString = action.input))
            }
            RemindsAction.EndSearch -> {
                pushState(currentState().copy(searching = false))
            }
            RemindsAction.HideNewReminder -> {
                pushState(currentState().copy(showNewReminder = false))
            }
            RemindsAction.ShowNewReminder -> {
                pushState(currentState().copy(showNewReminder = true))
            }
            is RemindsAction.AskForDelete -> {
                pushState(currentState().copy(reminderForDelete = action.reminder))
            }
            RemindsAction.CancelDelete -> {
                pushState(currentState().copy(reminderForDelete = null))
            }
            is RemindsAction.Delete -> {
                viewModelScope.launch(Dispatchers.IO){
                    deleteReminderUseCase(action.reminder)
                    pushState(currentState().copy(reminderForDelete = null))
                }
            }
            RemindsAction.ShowReminderTime -> {
                pushState(currentState().copy(showRemindTime = true))
            }
            RemindsAction.HideReminderTime -> {
                pushState(currentState().copy(showRemindTime = false))
            }
        }

    }

}
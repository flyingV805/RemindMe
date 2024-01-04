package kz.flyingv.remindme.features.reminds

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.domain.usecase.AddReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetRemindersUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemindsViewModel: KoinComponent, UIViewModel<RemindsState, RemindsAction> (
    RemindsState()
) {

    private val remindersUseCase: GetRemindersUseCase by inject()
    private val addReminderUseCase: AddReminderUseCase by inject()

    val remindersPaged = remindersUseCase().cachedIn(viewModelScope)

    init {

    }

    //remove later
    private fun testAdd(){
        viewModelScope.launch(Dispatchers.IO) {
            addReminderUseCase(
                Reminder(
                    name = "123123",
                    icon = ReminderIcon.Cake,
                    type = ReminderType.Daily,
                    action = ReminderAction.DoNothing,
                    lastShow = 0
                )
            )
        }
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
            RemindsAction.HideNewReminder -> {
                pushState(currentState().copy(showNewReminder = false))
            }
            RemindsAction.ShowNewReminder -> {
                testAdd()
                //pushState(currentState().copy(showNewReminder = true))
            }
        }

    }

}
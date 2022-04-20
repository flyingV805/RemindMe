package kz.flyingv.remindme.activity.main.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.model.RemindAction
import kz.flyingv.remindme.model.RemindIcon
import kz.flyingv.remindme.model.RemindType
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewReminderViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    private val _reminderNameText: MutableStateFlow<String> = MutableStateFlow("")
    /*
    val newReminderStateFlow: StateFlow<MainState> = combine(_currentReminders, _isSearching, _searchText){
            list, isSearch, searchText -> MainState(list, isSearch, searchText)
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())
*/
    fun makeAction(uiAction: NewReminderAction){

    }

    private fun createReminder(){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.addNewRemind(
                Reminder(
                    name = "",
                    icon = 12,
                    type = RemindType.Weekly(1),
                    action = RemindAction.OpenApp("", ""),
                    lastShow = 0
                )
            )
        }
    }

    private fun initialState(): NewReminderState {
        return NewReminderState(name = "", icon = RemindIcon.Cake)
    }

}
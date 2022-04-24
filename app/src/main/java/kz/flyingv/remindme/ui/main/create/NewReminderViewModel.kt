package kz.flyingv.remindme.ui.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.data.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewReminderViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    private val _reminderNameText: MutableStateFlow<String> = MutableStateFlow("")
    private val _reminderIcon: MutableStateFlow<RemindIcon> = MutableStateFlow(RemindIcon.Cake)

    val newReminderStateFlow: StateFlow<NewReminderState> =
        combine(_reminderNameText, _reminderIcon){name, icon ->
            NewReminderState(name = name, icon = icon)
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    fun makeAction(uiAction: NewReminderAction){
        when(uiAction){
            is NewReminderAction.UpdateName -> {
                _reminderNameText.value = uiAction.name
            }
            is NewReminderAction.UpdateIcon -> {
                _reminderIcon.value = uiAction.icon
            }
            NewReminderAction.CreateReminder -> {
                createReminder()
            }
        }
    }

    private fun createReminder(){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.addNewRemind(
                Reminder(
                    name = _reminderNameText.value,
                    icon = _reminderIcon.value,
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
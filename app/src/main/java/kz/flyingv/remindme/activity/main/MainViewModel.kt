package kz.flyingv.remindme.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kz.flyingv.remindme.model.RemindAction
import kz.flyingv.remindme.model.RemindPriority
import kz.flyingv.remindme.model.RemindType
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    private val _currentReminders = reminderRepository.getAllReminders()
    val currentReminders: StateFlow<List<Reminder>> = _currentReminders.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createReminder(){
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.addNewRemind(
                Reminder(
                    name = "",
                    description = "",
                    icon = 12,
                    avatarUri = "",
                    priority = RemindPriority.High,
                    type = RemindType.Daily,
                    action = RemindAction.OpenApp("", ""),
                    lastShow = 0,
                    time = 1
                )
            )
        }
    }

}
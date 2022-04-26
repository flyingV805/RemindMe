package kz.flyingv.remindme.ui.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.*
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.data.repository.SystemRepository
import kz.flyingv.remindme.ui.statemodel.RemindActionEnum
import kz.flyingv.remindme.ui.statemodel.RemindTypeEnum
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewReminderViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val systemRepository: SystemRepository by inject()

    private val _reminderNameText: MutableStateFlow<String> = MutableStateFlow("")
    private val _reminderIcon: MutableStateFlow<RemindIcon> = MutableStateFlow(RemindIcon.Cake)
    private val _reminderType: MutableStateFlow<RemindTypeEnum> = MutableStateFlow(RemindTypeEnum.Daily)
    private val _reminderAction: MutableStateFlow<RemindActionEnum> = MutableStateFlow(RemindActionEnum.Nothing)
    private val _availableApps: MutableStateFlow<List<InstalledApp>> = MutableStateFlow(emptyList())

    val newReminderStateFlow: StateFlow<NewReminderState> =
        combine(_reminderNameText, _reminderIcon, _reminderType, _reminderAction, _availableApps){name, icon, type, action, apps  ->
            NewReminderState(name = name, icon = icon, type = type, action = action, actionApps = apps)
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    fun makeAction(uiAction: NewReminderAction){
        when(uiAction){
            is NewReminderAction.UpdateName -> {
                _reminderNameText.value = uiAction.name
            }
            is NewReminderAction.UpdateIcon -> {
                _reminderIcon.value = uiAction.icon
            }
            is NewReminderAction.UpdateType -> {
                _reminderType.value = uiAction.remindType
            }
            is NewReminderAction.UpdateAction -> {
                _reminderAction.value = uiAction.remindAction
                if(uiAction.remindAction == RemindActionEnum.OpenApp){
                    viewModelScope.launch(Dispatchers.IO){
                        _availableApps.value = systemRepository.getInstalledApps()
                    }
                }
            }
            is NewReminderAction.CreateReminder -> {
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
        return NewReminderState(name = "", icon = RemindIcon.Cake, type = RemindTypeEnum.Daily, action = RemindActionEnum.Nothing, actionApps = emptyList())
    }

}
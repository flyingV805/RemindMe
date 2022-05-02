package kz.flyingv.remindme.ui.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.*
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.data.repository.SystemRepository
import kz.flyingv.remindme.ui.statemodel.ValidationError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewReminderViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val systemRepository: SystemRepository by inject()

    private val _reminderNameText: MutableStateFlow<String> = MutableStateFlow("")
    private val _reminderIcon: MutableStateFlow<RemindIcon> = MutableStateFlow(RemindIcon.Cake)
    private val _reminderType: MutableStateFlow<RemindType> = MutableStateFlow(RemindType.Daily)
    private val _reminderAction: MutableStateFlow<RemindAction> = MutableStateFlow(RemindAction.DoNothing)
    private val _availableApps: MutableStateFlow<List<InstalledApp>> = MutableStateFlow(emptyList())
    private val _validationError: MutableStateFlow<ValidationError> = MutableStateFlow(ValidationError.AllGood)
/*
    val newReminderStateFlow: StateFlow<NewReminderState> =
        combine(_reminderNameText, _reminderIcon, _reminderType, _reminderAction, _availableApps){name, icon, type, action, apps ->
            NewReminderState(
                name = name,
                icon = icon,
                type = type,
                action = action,
                actionApps = apps,
                error = validationError
            )
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())*/

    val newReminderStateFlow: StateFlow<NewReminderState> =
        combine(listOf(_reminderNameText, _reminderIcon, _reminderType, _reminderAction, _availableApps, _validationError).asIterable()){ flows ->
            NewReminderState(
                name = flows[0] as String,
                icon = flows[1] as RemindIcon,
                type = flows[2] as RemindType,
                action = flows[3] as RemindAction,
                actionApps = flows[4] as List<InstalledApp>,
                error = flows[5] as ValidationError
            )
        }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    fun makeAction(uiAction: NewReminderAction){
        when(uiAction){
            is NewReminderAction.UpdateName -> {
                _reminderNameText.value = uiAction.name
                _validationError.value = ValidationError.AllGood
            }
            is NewReminderAction.UpdateIcon -> {
                _reminderIcon.value = uiAction.icon
            }
            is NewReminderAction.UpdateType -> {
                _reminderType.value = uiAction.remindType
            }
            is NewReminderAction.UpdateAction -> {
                _reminderAction.value = uiAction.remindAction
                if(uiAction.remindAction is RemindAction.OpenApp){
                    if(_availableApps.value.isEmpty()){
                        viewModelScope.launch(Dispatchers.IO){
                            _availableApps.value = systemRepository.getInstalledApps()
                        }
                    }
                }
                _validationError.value = ValidationError.AllGood
            }
            is NewReminderAction.CreateReminder -> {
                createReminder()
            }
        }
    }

    private fun createReminder(){

        if(_reminderNameText.value.isBlank()){
            _validationError.value = ValidationError.NeedName
            return
        }

        when(val action = _reminderAction.value){
            RemindAction.DoNothing -> { }
            is RemindAction.OpenApp -> {
                if (action.installedApp == null){
                    _validationError.value = ValidationError.NeedApp
                    return
                }
            }
            is RemindAction.OpenUrl -> {
                if(action.url.isNullOrBlank()){
                    _validationError.value = ValidationError.NeedLink
                    return
                }
            }
        }
        _validationError.value = ValidationError.Created

        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.addNewRemind(
                Reminder(
                    name = _reminderNameText.value,
                    icon = _reminderIcon.value,
                    type = _reminderType.value,
                    action = _reminderAction.value,
                    lastShow = 0
                )
            )
            delay(500)
            _reminderNameText.value = ""
            _reminderIcon.value = RemindIcon.Cake
            _reminderType.value = RemindType.Daily
            _reminderAction.value = RemindAction.DoNothing
            _availableApps.value = emptyList()
            _validationError.value = ValidationError.AllGood
        }
    }

    private fun validate(){

    }

    private fun initialState(): NewReminderState {
        return NewReminderState(name = "", icon = RemindIcon.Cake, type = RemindType.Daily, action = RemindAction.DoNothing, actionApps = emptyList(), error = ValidationError.AllGood)
    }

}
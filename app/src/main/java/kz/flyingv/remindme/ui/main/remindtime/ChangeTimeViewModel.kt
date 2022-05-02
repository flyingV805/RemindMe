package kz.flyingv.remindme.ui.main.remindtime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.RemindTime
import kz.flyingv.remindme.data.repository.SchedulerRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChangeTimeViewModel: ViewModel(), KoinComponent {

    private val schedulerRepository: SchedulerRepository by inject()

    private var currentTime: RemindTime = schedulerRepository.currentRemindTime()

    private val _changeTimeStateFlow: MutableStateFlow<ChangeTimeState> = MutableStateFlow(ChangeTimeState(currentTime))

    val changeTimeStateFlow: StateFlow<ChangeTimeState> = _changeTimeStateFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, ChangeTimeState(currentTime))

    fun makeAction(uiAction: ChangeTimeAction){
        when(uiAction){
            is ChangeTimeAction.SaveTime -> {
                _changeTimeStateFlow.value = ChangeTimeState(currentTime)
                viewModelScope.launch(Dispatchers.IO){
                    schedulerRepository.reschedule(currentTime)
                }
            }
            is ChangeTimeAction.GetTime -> {
                _changeTimeStateFlow.value = ChangeTimeState(schedulerRepository.currentRemindTime())
            }
            is ChangeTimeAction.UpdateTime -> {
                currentTime = RemindTime(uiAction.hour, uiAction.minute)
            }
        }
    }



}
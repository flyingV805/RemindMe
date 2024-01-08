package kz.flyingv.remindme.features.remindtime

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.ReminderTime
import kz.flyingv.remindme.domain.usecase.GetReminderTimeUseCase
import kz.flyingv.remindme.domain.usecase.RescheduleUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RemindTimeViewModel: KoinComponent, UIViewModel<RemindTimeState, RemindTimeAction>(
    RemindTimeState()
) {

    private val getReminderTimeUseCase: GetReminderTimeUseCase by inject()
    private val rescheduleUseCase: RescheduleUseCase by inject()

    init {
        val remindTime = getReminderTimeUseCase()
        pushState(currentState().copy(selectHour = remindTime.hour, selectMinute = remindTime.minute))
    }

    override fun reduce(action: RemindTimeAction) {
        super.reduce(action)

        when(action){
            is RemindTimeAction.UpdateTime -> {
                viewModelScope.launch(Dispatchers.IO){
                    rescheduleUseCase.invoke(ReminderTime(action.hour, action.minute))
                    pushState(currentState().copy(selectHour = action.hour, selectMinute = action.minute, hide = true))
                }
            }
            RemindTimeAction.HideDialog -> {
                pushState(currentState().copy(hide = true))
            }
            RemindTimeAction.Hidden -> {
                pushState(currentState().copy(hide = false))
            }
        }

    }

}
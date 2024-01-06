package kz.flyingv.remindme.features.create

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.domain.usecase.AddReminderUseCase
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.ValidationError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewRemindViewModel: KoinComponent, UIViewModel<NewRemindState, NewRemindAction>(
    initialState = NewRemindState()
) {

    private val addReminderUseCase: AddReminderUseCase by inject()

    override fun reduce(action: NewRemindAction) {
        super.reduce(action)

        when(action){
            is NewRemindAction.UpdateName -> {
                pushState( currentState().copy(name = action.name) )
            }
            is NewRemindAction.UpdateIcon -> {
                pushState( currentState().copy(icon = action.icon) )
            }
            is NewRemindAction.UpdateAction -> {
                pushState( currentState().copy(action = action.action) )
            }
            is NewRemindAction.UpdateType -> {
                pushState( currentState().copy(type = action.type) )
            }
            NewRemindAction.Create -> {
                val currentState = currentState()
                val errors = arrayListOf<ValidationError>()

                if(currentState.name.isBlank()){ errors.add(ValidationError.NeedName) }

                if(currentState.action == RemindAction.OpenApp && currentState.actionApp == null ){
                    errors.add(ValidationError.NeedApp)
                }

                if(currentState.action == RemindAction.OpenUrl && currentState.actionUrl.isBlank() ){
                    errors.add(ValidationError.NeedLink)
                }

                if(errors.isNotEmpty()){
                    pushState(currentState.copy(errors = errors))
                    return
                }

                viewModelScope.launch(Dispatchers.IO) {
                    addReminderUseCase(
                        Reminder(
                            name = currentState.name,
                            icon = currentState.icon,
                            type = ReminderType.Daily,
                            action = ReminderAction.DoNothing,
                            lastShow = 0
                        )
                    )
                }

            }
        }

    }

}
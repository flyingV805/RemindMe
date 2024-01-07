package kz.flyingv.remindme.features.create

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.domain.entity.ReminderType
import kz.flyingv.remindme.domain.usecase.AddReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetInstalledAppsUseCase
import kz.flyingv.remindme.features.create.uidata.RemindAction
import kz.flyingv.remindme.features.create.uidata.RemindType
import kz.flyingv.remindme.features.create.uidata.ValidationError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NewRemindViewModel: KoinComponent, UIViewModel<NewRemindState, NewRemindAction>(
    initialState = NewRemindState()
) {

    private val getInstalledAppsUseCase: GetInstalledAppsUseCase by inject()
    private val addReminderUseCase: AddReminderUseCase by inject()

    init {
        viewModelScope.launch(Dispatchers.IO){
            getInstalledAppsUseCase().collectLatest {
                pushState(currentState().copy(availableApps = it))
            }
        }
    }

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
            is NewRemindAction.UpdateApp -> {
                pushState( currentState().copy(actionApp = action.app) )
            }
            is NewRemindAction.UpdateLink -> {
                pushState( currentState().copy(actionUrl = action.url) )
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

                val reminderType = when(currentState.type){
                    RemindType.Daily -> ReminderType.Daily
                    RemindType.Weekly -> ReminderType.Weekly(dayOfWeek = currentState.dayOfWeek)
                    RemindType.Monthly -> ReminderType.Monthly(dayOfMonth = currentState.dayOfMonth)
                    RemindType.Yearly -> ReminderType.Yearly(dayOfMonth = currentState.dayOfYear, month = currentState.monthOfYear)
                }

                val reminderAction = when(currentState.action){
                    RemindAction.Nothing -> ReminderAction.DoNothing
                    RemindAction.OpenApp -> ReminderAction.OpenApp(currentState.actionApp)
                    RemindAction.OpenUrl -> ReminderAction.OpenUrl(currentState.actionUrl)
                }

                viewModelScope.launch(Dispatchers.IO) {
                    addReminderUseCase(
                        Reminder(
                            name = currentState.name,
                            icon = currentState.icon,
                            type = reminderType,
                            action = reminderAction,
                            lastShow = 0
                        )
                    )
                    pushState(currentState.copy(done = true))
                }

            }

            NewRemindAction.Hidden -> {
                pushState( currentState().copy(done = false) )
            }

        }

    }

}
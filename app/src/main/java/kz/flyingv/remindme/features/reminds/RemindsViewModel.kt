package kz.flyingv.remindme.features.reminds

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kz.flyingv.cleanmvi.UIViewModel
import kz.flyingv.remindme.domain.usecase.DeleteReminderUseCase
import kz.flyingv.remindme.domain.usecase.GetCurrentUserUseCase
import kz.flyingv.remindme.domain.usecase.GetIsAlarmsPermittedUseCase
import kz.flyingv.remindme.domain.usecase.GetRemindersUseCase
import kz.flyingv.remindme.domain.usecase.SearchRemindersUseCase
import kz.flyingv.remindme.domain.usecase.SyncRemindsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(FlowPreview::class)
class RemindsViewModel: KoinComponent, UIViewModel<RemindsState, RemindsAction> (
    RemindsState()
) {

    private val context: Context by inject()

    private val alarmsPermittedUseCase: GetIsAlarmsPermittedUseCase by inject()
    private val remindersUseCase: GetRemindersUseCase by inject()
    private val searchRemindersUseCase: SearchRemindersUseCase by inject()
    private val deleteReminderUseCase: DeleteReminderUseCase by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val syncRemindsUseCase: SyncRemindsUseCase by inject()

    private val searchFlow = MutableSharedFlow<String>()

    val remindersPaged = remindersUseCase().cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO){
            searchFlow.debounce(300).collectLatest { searchFor ->
                if(searchFor.isBlank()){
                    pushState(currentState().copy(searchReminds = emptyList()))
                    return@collectLatest
                }
                val searchResult = searchRemindersUseCase(searchFor)
                Log.w("search result", searchFor.plus(": ").plus(searchResult))
                pushState(currentState().copy(searchReminds = searchResult))
            }
        }

        updateProfile()
        syncReminds()
    }

    private fun updateProfile(){
        viewModelScope.launch(Dispatchers.IO){
            val currentUser = getCurrentUserUseCase()
            pushState(
                currentState().copy(
                    authorized = currentUser != null,
                    avatarUrl = currentUser?.avatarUrl ?: ""
                )
            )
        }
    }

    private fun syncReminds(){
        viewModelScope.launch(Dispatchers.IO){
            pushState(currentState().copy(syncInProgress = true))
            val syncResult = syncRemindsUseCase()
            pushState(currentState().copy(syncInProgress = false))
        }
    }

    override fun reduce(action: RemindsAction) {
        super.reduce(action)

        when(action){
            RemindsAction.CheckPermissions -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val askForNotifications = context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED

                    val askForAlarmSchedule = !alarmsPermittedUseCase()

                    pushState(currentState().copy(
                        askNotificationPermissions = askForNotifications,
                        askAlarmPermissions = askForAlarmSchedule
                    ))

                }

            }
            RemindsAction.HidePermissionDialog -> {
                pushState(currentState().copy(askNotificationPermissions = false, askAlarmPermissions = false))
            }
            RemindsAction.StartSearch -> {
                pushState(currentState().copy(searching = true))
            }
            is RemindsAction.Search -> {
                pushState(currentState().copy(searchString = action.input))
                viewModelScope.launch { searchFlow.emit(action.input) }
            }
            RemindsAction.EndSearch -> {
                pushState(currentState().copy(searching = false, searchString = "", searchReminds = emptyList()))
                viewModelScope.launch { searchFlow.emit("") }
            }
            RemindsAction.HideNewReminder -> {
                pushState(currentState().copy(showNewReminder = false))
            }
            RemindsAction.ShowNewReminder -> {
                pushState(currentState().copy(showNewReminder = true))
            }
            is RemindsAction.AskForDelete -> {
                pushState(currentState().copy(reminderForDelete = action.reminder))
            }
            RemindsAction.CancelDelete -> {
                pushState(currentState().copy(reminderForDelete = null))
            }
            is RemindsAction.Delete -> {
                viewModelScope.launch(Dispatchers.IO){
                    deleteReminderUseCase(action.reminder)
                    pushState(currentState().copy(reminderForDelete = null))
                }
            }
            RemindsAction.ShowReminderTime -> {
                pushState(currentState().copy(showRemindTime = true))
            }
            RemindsAction.HideReminderTime -> {
                pushState(currentState().copy(showRemindTime = false))
            }
            RemindsAction.ShowProfile -> {
                pushState(currentState().copy(showProfileDialog = true))
            }
            RemindsAction.HideProfile -> {
                pushState(currentState().copy(showProfileDialog = false))
                updateProfile()
                syncReminds()
            }
        }

    }

}
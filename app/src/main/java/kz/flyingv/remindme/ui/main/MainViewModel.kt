package kz.flyingv.remindme.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.data.repository.SchedulerRepository
import kz.flyingv.remindme.utils.datetime.DatetimeUtils
import kz.flyingv.remindme.utils.notifications.Notificator
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.util.*

class MainViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()
    private val schedulerRepository: SchedulerRepository by inject()

    private val _currentReminders = reminderRepository.getAllReminders()
    private val _isSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")

    val mainStateFlow: StateFlow<MainState> =
        combine(_currentReminders, _isSearching, _searchText){list, isSearch, searchText ->

            val remindersList = if(searchText.isNotBlank()){
                list.filter { it.name.contains(searchText) }
            }else{
                list
            }

            MainState(remindersList, isSearch, searchText)
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    init {
        viewModelScope.launch(Dispatchers.IO){
            schedulerRepository.initReminderIfNeeded()
        }
    }

    fun makeAction(uiAction: MainAction){
        when(uiAction){
            MainAction.StartSearch -> {
                _isSearching.value = true
            }
            MainAction.EndSearch -> {
                _isSearching.value = false
                _searchText.value = ""
            }
            is MainAction.UpdateSearch -> {
                updateSearch(uiAction.text)
            }
            is MainAction.DeleteReminder -> {
                viewModelScope.launch(Dispatchers.IO){
                    reminderRepository.deleteRemind(uiAction.reminder)
                }
            }
        }
    }

    private fun initialState(): MainState {
        return MainState(emptyList(), false, "", true)
    }

    private fun updateSearch(searchText: String){
        _searchText.value = searchText
    }

    //debug section
    fun debugNotification(context: Context, reminder: Reminder){
        val notificator = Notificator(context = context)
        val currentDate = Calendar.getInstance()
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.getWorkerReminders().forEach { reminder ->

            }
        }

    }

}
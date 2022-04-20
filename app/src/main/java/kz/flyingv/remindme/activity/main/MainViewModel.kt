package kz.flyingv.remindme.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.flyingv.remindme.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    private val _currentReminders = reminderRepository.getAllReminders()
    private val _isSearching: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _searchText: MutableStateFlow<String> = MutableStateFlow("")

    val mainStateFlow: StateFlow<MainState> =
        combine(_currentReminders, _isSearching, _searchText){list, isSearch, searchText ->
            MainState(list, isSearch, searchText)
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    init {
        viewModelScope.launch(Dispatchers.IO){
            reminderRepository.initReminderIfNeeded()
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
        }
    }

    private fun initialState(): MainState {
        return MainState(emptyList(), false, "")
    }

    private fun updateSearch(searchText: String){
        _searchText.value = searchText
    }

}
package kz.flyingv.remindme.ui.main

import kz.flyingv.remindme.data.model.Reminder

data class MainState(
    val reminders: List<Reminder>,
    val isSearching: Boolean,
    val searchText: String,
    val isInitial: Boolean = false
) {


}
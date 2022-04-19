package kz.flyingv.remindme.activity.main

import kz.flyingv.remindme.model.Reminder

data class MainState(
    val reminders: List<Reminder>,
    val isSearching: Boolean,
    val searchText: String
) {


}
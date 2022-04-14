package kz.flyingv.remindme.activity.main.state

import kz.flyingv.remindme.model.Reminder

data class MainState(
    val reminders: List<Reminder>,
    val isSearching: Boolean,
    val searchText: String
) {


}
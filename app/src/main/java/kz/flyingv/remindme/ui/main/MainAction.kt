package kz.flyingv.remindme.ui.main

import kz.flyingv.remindme.data.model.Reminder

sealed class MainAction {

    object StartSearch: MainAction()
    class UpdateSearch(val text: String): MainAction()
    object EndSearch: MainAction()
    class DeleteReminder(val reminder: Reminder): MainAction()

}

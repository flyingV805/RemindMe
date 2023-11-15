package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIAction

sealed class RemindsAction: UIAction {

    data object StartSearch: RemindsAction()
    data class Search(val input: String): RemindsAction()
    data object EndSearch: RemindsAction()

}
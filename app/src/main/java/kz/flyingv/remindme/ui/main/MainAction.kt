package kz.flyingv.remindme.ui.main

sealed class MainAction {

    object StartSearch: MainAction()
    class UpdateSearch(val text: String): MainAction()
    object EndSearch: MainAction()

}

package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIAction

sealed class NewRemindAction: UIAction {

    data object Create: NewRemindAction()

}

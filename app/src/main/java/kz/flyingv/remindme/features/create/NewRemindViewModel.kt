package kz.flyingv.remindme.features.create

import kz.flyingv.cleanmvi.UIViewModel

class NewRemindViewModel: UIViewModel<NewRemindState, NewRemindAction>(
    initialState = NewRemindState()
) {

    override fun reduce(action: NewRemindAction) {
        super.reduce(action)

    }

}
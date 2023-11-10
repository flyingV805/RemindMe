package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIViewModel

class RemindsViewModel: UIViewModel<RemindsState, RemindsAction>(
    RemindsState()
) {

    override fun reduce(action: RemindsAction) {
        super.reduce(action)
    }

}
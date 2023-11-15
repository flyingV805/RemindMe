package kz.flyingv.remindme.features.reminds

import kz.flyingv.cleanmvi.UIViewModel

class RemindsViewModel: UIViewModel<RemindsState, RemindsAction>(
    RemindsState()
) {

    override fun reduce(action: RemindsAction) {
        super.reduce(action)

        when(action){
            RemindsAction.StartSearch -> {
                pushState(currentState().copy(searching = true))
            }
            is RemindsAction.Search -> {

            }
            RemindsAction.EndSearch -> {
                pushState(currentState().copy(searching = false))
            }
        }

    }

}
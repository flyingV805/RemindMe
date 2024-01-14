package kz.flyingv.remindme.domain.usecase

import kotlinx.coroutines.delay
import kz.flyingv.remindme.domain.entity.SyncResult
import org.koin.core.component.KoinComponent

class SyncRemindsUseCase: KoinComponent {

    suspend operator fun invoke(): SyncResult {
        delay(5000)
        return SyncResult.Success
    }

}
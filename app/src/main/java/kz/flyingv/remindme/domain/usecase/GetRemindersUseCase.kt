package kz.flyingv.remindme.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kz.flyingv.remindme.data.repository.ReminderRepository
import kz.flyingv.remindme.domain.entity.Reminder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRemindersUseCase: KoinComponent {

    private val reminderRepository: ReminderRepository by inject()

    operator fun invoke(): Flow<PagingData<Reminder>> {
        return reminderRepository.getAllRemindersPaged()
    }

}
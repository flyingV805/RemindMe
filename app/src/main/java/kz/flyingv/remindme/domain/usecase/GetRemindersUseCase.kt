package kz.flyingv.remindme.domain.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kz.flyingv.remindme.domain.entity.Reminder

class GetRemindersUseCase {

    suspend operator fun invoke()/*: Flow<PagingData<Reminder>>*/ {
        /*return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                initialLoadSize = 20,
            ),
            pagingSourceFactory = {
                //messagesRepository.getMessagesPaged(chat)
            }
        ).flow {

        }*/
    }

}
package kz.flyingv.remindme.repository

import kz.flyingv.remindme.room.Database
import kz.flyingv.remindme.scheduler.RemindScheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface RemindRepository {



}

class RemindRepositoryImpl: RemindRepository, KoinComponent {

    private val database: Database by inject()
    private val scheduler: RemindScheduler by inject()

}
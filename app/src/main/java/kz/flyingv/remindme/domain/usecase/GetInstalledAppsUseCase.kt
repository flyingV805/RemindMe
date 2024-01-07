package kz.flyingv.remindme.domain.usecase

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.data.repository.SystemRepository
import kz.flyingv.remindme.domain.entity.InstalledApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetInstalledAppsUseCase: KoinComponent {

    private val systemRepository: SystemRepository by inject()

    suspend operator fun invoke(): Flow<List<InstalledApp>> {
        return systemRepository.getInstalledAppsAsFlow()
    }

}
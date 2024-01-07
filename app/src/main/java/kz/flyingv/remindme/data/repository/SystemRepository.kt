package kz.flyingv.remindme.data.repository

import kotlinx.coroutines.flow.Flow
import kz.flyingv.remindme.domain.entity.InstalledApp


interface SystemRepository {

    suspend fun getInstalledApps(): List<InstalledApp>
    suspend fun getInstalledAppsCached(): List<InstalledApp>
    suspend fun getInstalledAppsAsFlow(): Flow<List<InstalledApp>>

}
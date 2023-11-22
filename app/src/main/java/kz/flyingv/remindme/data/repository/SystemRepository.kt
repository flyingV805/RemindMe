package kz.flyingv.remindme.data.repository

import kz.flyingv.remindme.domain.entity.InstalledApp


interface SystemRepository {

    fun getInstalledApps(): List<InstalledApp>

}
package kz.flyingv.remindme.data.repository

import android.content.Context
import android.content.Intent
import kz.flyingv.remindme.domain.entity.InstalledApp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//the repository for interactions with OS
class SystemRepositoryImpl: SystemRepository, KoinComponent {

    private val context: Context by inject()

    override fun getInstalledApps(): List<InstalledApp> {
        val packageManager = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfo = packageManager.queryIntentActivities(mainIntent, 0)

        return resolveInfo.map {
            val appRes = packageManager.getResourcesForApplication(it.activityInfo.applicationInfo)

            val appName = if(it.activityInfo.labelRes != 0){
                appRes.getString(it.activityInfo.labelRes)
            }else{
                it.activityInfo.applicationInfo.loadLabel(packageManager).toString()
            }

            val appIcon = packageManager.getApplicationIcon(it.activityInfo.applicationInfo)

            InstalledApp(
                name = appName,
                icon = appIcon,
                launchActivity = it.activityInfo.packageName
            )
        }

    }

}
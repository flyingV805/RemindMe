package kz.flyingv.remindme.data.datastore.mapper

import kz.flyingv.remindme.domain.entity.InstalledApp
import kz.flyingv.remindme.domain.entity.ReminderAction
import org.json.JSONObject
import java.util.Calendar

class ReminderActionMapper {

    companion object {

        private const val typeOpenApp = 0
        private const val typeOpenUrl = 1
        private const val typeDoNothing = 2

        fun mapToString(action: ReminderAction): String {
            return when(action){
                is ReminderAction.OpenApp -> {
                    JSONObject()
                        .put("type", typeOpenApp)
                        .put("appName", action.installedApp?.name ?: "")
                        .put("package", action.installedApp?.launchActivity ?: "")
                        .toString()
                }
                is ReminderAction.OpenUrl -> {
                    JSONObject()
                        .put("type", typeOpenUrl)
                        .put("url", action.url)
                        .toString()
                }
                is ReminderAction.DoNothing -> {
                    JSONObject()
                        .put("type", typeDoNothing)
                        .toString()
                }
            }
        }

        fun mapFromString(data: String): ReminderAction? {
            return try {
                val jsonObject = JSONObject(data)
                when(jsonObject.optInt("type", -1)){
                    typeOpenApp -> {
                        val installedApp = InstalledApp(
                            name = jsonObject.getString("appName"),
                            launchActivity = jsonObject.getString("package"),
                            icon = null
                        )
                        ReminderAction.OpenApp(installedApp)
                    }
                    typeOpenUrl -> ReminderAction.OpenUrl(jsonObject.getString("url"))
                    typeDoNothing -> ReminderAction.DoNothing
                    else -> null
                }
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }

    }

}
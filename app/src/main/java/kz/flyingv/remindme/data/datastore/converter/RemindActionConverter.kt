package kz.flyingv.remindme.data.datastore.converter

import androidx.room.TypeConverter
import kz.flyingv.remindme.data.model.RemindAction
import org.json.JSONObject
import java.lang.Exception

class RemindActionConverter {

    @TypeConverter
    fun from(action: RemindAction?): String? {
        return when(action){
            is RemindAction.OpenApp -> {JSONObject().put("type", typeOpenApp).put("appName", action.appName).put("package", action.appPackage).toString()}
            is RemindAction.OpenUrl -> {JSONObject().put("type", typeOpenUrl).put("url", action.url).toString()}
            is RemindAction.DoNothing -> {JSONObject().put("type", typeDoNothing).toString()}
            null -> null
        }
    }

    @TypeConverter
    fun to(data: String): RemindAction? {
        return try {
            val jsonObject = JSONObject(data ?: "")
            when(jsonObject.optInt("type", -1)){
                typeOpenApp -> RemindAction.OpenApp(jsonObject.getString("appName"), jsonObject.getString("package"))
                typeOpenUrl -> RemindAction.OpenUrl(jsonObject.getString("url"))
                typeDoNothing -> RemindAction.DoNothing
                else -> null
            }
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    companion object {

        const val typeOpenApp = 0
        const val typeOpenUrl = 1
        const val typeDoNothing = 2
    }

}
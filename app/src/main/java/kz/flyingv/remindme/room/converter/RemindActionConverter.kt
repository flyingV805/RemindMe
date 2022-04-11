package kz.flyingv.remindme.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import kz.flyingv.remindme.model.RemindAction
import java.lang.Exception

class RemindActionConverter {

    @TypeConverter
    fun from(action: RemindAction?): String? {
        return Gson().toJson(action)
    }

    @TypeConverter
    fun to(data: String): RemindAction? {
        return try {
            Gson().fromJson(data, RemindAction::class.java)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

}
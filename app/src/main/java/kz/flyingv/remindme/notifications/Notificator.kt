package kz.flyingv.remindme.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kz.flyingv.remindme.R

class Notificator(private val context: Context) {

    fun makeTestNotification(){
        initNotificationChannel()

        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setContentTitle("Remind me test")
            .setContentText("Remind me test")
            .setColor(ContextCompat.getColor(context, R.color.purple_700))
            .setStyle(NotificationCompat.BigTextStyle().bigText("Remind me test"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setChannelId(notificationChannelId)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        NotificationManagerCompat.from(context).notify(521, builder.build())

    }

    private fun initNotificationChannel(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (isNotificationChannelExists(notificationManager)){return;}
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notificationChannelId, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification channel for Reminder"
            channel.lightColor = Color.GREEN
            channel.enableLights(true)
            channel.enableVibration(true)
            //channel.setSound(sound, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isNotificationChannelExists(manager: NotificationManager):Boolean{
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel: NotificationChannel? = manager.getNotificationChannel(notificationChannelId)
            channel?.let { return true }
            return false
        }else{
            return true
        }
    }

    companion object {

        const val notificationChannelId = "RemindMeNotifications"

    }

}
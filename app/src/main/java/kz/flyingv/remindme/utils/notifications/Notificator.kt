package kz.flyingv.remindme.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kz.flyingv.remindme.R
import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindIcon
import kz.flyingv.remindme.data.model.Reminder

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

    fun showNotification(reminder: Reminder){
        initNotificationChannel()
        /*
        val smallIcon = when(reminder.icon){

        }*/

        val smallIcon = when(reminder.icon){
            RemindIcon.Cake -> R.drawable.ic_notification_cake
            RemindIcon.Medicine -> R.drawable.ic_notification_medecine
            RemindIcon.Officials -> R.drawable.ic_notification_officials
            RemindIcon.Payday -> R.drawable.ic_notification_payday
            RemindIcon.Workout -> R.drawable.ic_notification_workout
        }

        val largeIcon = when(reminder.icon){
            RemindIcon.Cake -> R.drawable.ic_avatar_cake
            RemindIcon.Medicine -> R.drawable.ic_avatar_medecine
            RemindIcon.Officials -> R.drawable.ic_avatar_officials
            RemindIcon.Payday -> R.drawable.ic_avatar_payday
            RemindIcon.Workout -> R.drawable.ic_avatar_workout
        }

        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
            .setContentTitle(reminder.name)
            .setContentText("Remind me test")
            .setColor(ContextCompat.getColor(context, R.color.purple_700))
            .setStyle(NotificationCompat.BigTextStyle().bigText("Remind me test"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setContentIntent(contentIntent)
            .setAutoCancel(false)
            .setChannelId(notificationChannelId)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            //.addAction(R.drawable.ic_launcher_foreground, "", snoozePendingIntent);

        when(reminder.action){
            is RemindAction.OpenApp -> {
                val openAppIntent = context.packageManager.getLaunchIntentForPackage(reminder.action.installedApp?.launchActivity ?: "")
                openAppIntent?.let {
                    val snoozePendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, 0)
                    builder.addAction(R.drawable.ic_baseline_alarm_24, context.getString(R.string.open_link), snoozePendingIntent)
                }
            }
            is RemindAction.OpenUrl -> {
                val openLinkIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(reminder.action.url)
                }
                val snoozePendingIntent = PendingIntent.getActivity(context, 0, openLinkIntent, 0)
                builder.addAction(R.drawable.ic_baseline_alarm_24, context.getString(R.string.open_link), snoozePendingIntent)
            }
            else -> {}
        }

        NotificationManagerCompat.from(context).notify(reminder.id, builder.build())
    }

    private fun initNotificationChannel(){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (isNotificationChannelExists(notificationManager)){return;}
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(notificationChannelId, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Reminder notifications"
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
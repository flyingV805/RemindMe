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
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.domain.entity.ReminderAction
import kz.flyingv.remindme.domain.entity.ReminderIcon
import kz.flyingv.remindme.ui.utils.RemindFormatter

class Notificator(private val context: Context) {

    fun showNotification(reminder: Reminder){
        initNotificationChannel()

        val smallIcon = when(reminder.icon){
            ReminderIcon.Cake -> R.drawable.ic_notification_cake
            ReminderIcon.Medicine -> R.drawable.ic_notification_medecine
            ReminderIcon.Officials -> R.drawable.ic_notification_officials
            ReminderIcon.Payday -> R.drawable.ic_notification_payday
            ReminderIcon.Workout -> R.drawable.ic_notification_workout
        }

        val largeIcon = when(reminder.icon){
            ReminderIcon.Cake -> R.drawable.ic_avatar_cake
            ReminderIcon.Medicine -> R.drawable.ic_avatar_medecine
            ReminderIcon.Officials -> R.drawable.ic_avatar_officials
            ReminderIcon.Payday -> R.drawable.ic_avatar_payday
            ReminderIcon.Workout -> R.drawable.ic_avatar_workout
        }

        val notificationText = RemindFormatter.formatRemindType(reminder.type)

        val builder = NotificationCompat.Builder(context, notificationChannelId)
            .setSmallIcon(smallIcon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
            .setContentTitle(reminder.name)
            .setContentText(notificationText)
            .setColor(ContextCompat.getColor(context, R.color.purple_700))
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setChannelId(notificationChannelId)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        when(reminder.action){
            is ReminderAction.OpenApp -> {
                val openAppIntent = context.packageManager.getLaunchIntentForPackage(reminder.action.installedApp?.launchActivity ?: "")
                openAppIntent?.let {
                    val snoozePendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE )
                    builder.addAction(largeIcon, context.getString(R.string.open_app), snoozePendingIntent)
                }
            }
            is ReminderAction.OpenUrl -> {
                val openLinkIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(reminder.action.url)
                }
                val snoozePendingIntent = PendingIntent.getActivity(context, 0, openLinkIntent, PendingIntent.FLAG_IMMUTABLE)
                builder.addAction(largeIcon, context.getString(R.string.open_link), snoozePendingIntent)
            }
            else -> {}
        }

        //NotificationManagerCompat.from(context).notify(reminder.id, builder.build())
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
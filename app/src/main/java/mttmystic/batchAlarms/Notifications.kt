package mttmystic.batchAlarms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class Notifications @Inject constructor(@ApplicationContext context : Context) {

    //private var pendingNotifications : List<Int> = mutableListOf()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private lateinit var alarmPlayer : MediaPlayer

    init {
        createNotificationChannel(context)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            val name = "Alarms" //todo replace with string resource
            val descriptionText : String = getString(context, R.string.notification_channel_description)

            val importance = NotificationManager.IMPORTANCE_HIGH

            val channelID = getString(context, R.string.notification_channel_id)

            val channel = NotificationChannel(channelID, name, importance)

            val notificationManager : NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun playAlarmSound(context: Context) {
        val alarmUri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        alarmPlayer = MediaPlayer.create(context, alarmUri).apply {
            isLooping = true
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            start()
        }
    }

    fun stopAlarmSound() {
        alarmPlayer.stop()
        alarmPlayer.release()
    }

    fun getNotifyAlarmIntent(context : Context) : Intent {
        /*val notifyIntent = Intent(
            context,
            AlarmActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }*/
        val notifyIntent = Intent(
            context,
            AlarmReceiver::class.java
        )

        return notifyIntent
    }

    fun getNotifyPendingIntent(context : Context, intent : Intent) : PendingIntent {
        /*val notifyPendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )*/
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return notifyPendingIntent
    }

    fun getStopAlarmIntent(context: Context, alarm_id : Int?) : Intent {
        val stopAlarmIntent = Intent(
            context,
            StopAlarmReceiver::class.java).apply {
            action = "STOP_ALARM"
            putExtra("alarm_id", alarm_id)
        }

        return stopAlarmIntent
    }

    fun getStopAlarmPendingIntent(context: Context, intent: Intent) : PendingIntent{
        val stopAlarmPendingIntent  = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return stopAlarmPendingIntent
    }


    fun getNotificationBuilder(context : Context, title : String,  text : String?, alarmID : Int?) : NotificationCompat.Builder {
        val notifyIntent = getNotifyAlarmIntent(context)
        val notifyPendingIntent = getNotifyPendingIntent(context, notifyIntent)

        val stopAlarmIntent = getStopAlarmIntent(context, alarmID)
        val stopAlarmPendingIntent = getStopAlarmPendingIntent(context, stopAlarmIntent)
        val channelID : String = getString(context, R.string.notification_channel_id)
        val builder = NotificationCompat.Builder(context, channelID )
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setFullScreenIntent(notifyPendingIntent, true)
            .addAction(0, getString(context, R.string.stop_alarm_text), stopAlarmPendingIntent)
        return builder
    }

    fun generateNotification(context: Context, alarm_time:String?, alarm_id:Int) {

        val title = getString(context, R.string.alarm_notification_title)
        val text = alarm_time
        val builder = getNotificationBuilder(context, title, alarm_time, alarm_id)
        //pendingNotifications = pendingNotifications.filterNot {it != alarm_id}
        //pendingNotifications += alarm_id
        notificationManager.notify(666, builder.build())
        playAlarmSound(context)
    }

    fun cancelNotification(context : Context, alarm_id: Int) {
        //notificationManager.cancel(alarm_id)
        notificationManager.cancel(666)
        stopAlarmSound()
    }


}
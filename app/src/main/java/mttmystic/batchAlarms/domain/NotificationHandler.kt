package mttmystic.batchAlarms.domain

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
import mttmystic.batchAlarms.AlarmReceiver
import mttmystic.batchAlarms.R
import mttmystic.batchAlarms.StopAlarmReceiver

const val channelIdName = "alarms"
const val channelIdNum = 666

/*
interface MediaPlayerFactory {
    fun create(uri: Uri) : MediaPlayer
}

class MediaPlayerFactoryImpl @Inject constructor(
    @ApplicationContext private val context : Context
) : MediaPlayerFactory {
    override fun create(uri : Uri) : MediaPlayer{
        return MediaPlayer.create(context, uri).apply {
            isLooping = true
            setAudioAttributes(

                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
        }
    }
}
*/

interface NotificationHandler {
    fun showNotification(
        timeString: String,
        uri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    )

    fun cancelNotification()
}

@Singleton
class NotificationHandlerImpl @Inject constructor(
    @ApplicationContext private val context : Context) : NotificationHandler
{
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    //TODO add a way to customize the sound
    //private val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    private lateinit var alarmPlayer : MediaPlayer

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            val name = getString(context, R.string.notification_channel_name) //todo replace with string resource
            //val descriptionText : String = getString(context, R.string.notification_channel_description)

            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelIdName, name, importance)

            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getShowNotificationIntent() : PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return notifyPendingIntent

    }

    fun getCancelNotificationIntent() : PendingIntent {
        val intent = Intent(context, StopAlarmReceiver::class.java)
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return cancelPendingIntent
    }

    fun getNotificationBuilder(title : String,  text : String?) : NotificationCompat.Builder {
        val notifyIntent = getShowNotificationIntent()
        val cancelIntent = getCancelNotificationIntent()
        val builder = NotificationCompat.Builder(context, channelIdName)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setFullScreenIntent(notifyIntent, true)
            .addAction(0, getString(context, R.string.stop_alarm_text), cancelIntent)
        return builder
    }

    fun playAlarmSound(context: Context) {
        alarmPlayer.start()
    }

    fun stopAlarmSound() {
        alarmPlayer.stop()
        alarmPlayer.release()
    }

    override fun showNotification (timeString: String, uri : Uri) {
        val title = getString(context, R.string.alarm_notification_title)
        val builder = getNotificationBuilder(title, timeString)
        //pendingNotifications = pendingNotifications.filterNot {it != alarm_id}
        //pendingNotifications += alarm_id
        notificationManager.notify(channelIdNum, builder.build())
        alarmPlayer = MediaPlayer.create(context, uri).apply {
            isLooping = true
            setAudioAttributes(

                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )

        }
        playAlarmSound(context)
    }

    override fun cancelNotification() {
        notificationManager.cancel(channelIdNum)
        stopAlarmSound()
    }

}

@Singleton
class oldNotificationHandler @Inject constructor(@ApplicationContext context : Context) {

    //private var pendingNotifications : List<Int> = mutableListOf()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    //TODO add a way to customize this
    private var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    private var alarmPlayer : MediaPlayer = MediaPlayer.create(context, alarmUri).apply {
        isLooping = true
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )

    }

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
        alarmPlayer.start()
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
            putExtra("alarm_id",  alarm_id)
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
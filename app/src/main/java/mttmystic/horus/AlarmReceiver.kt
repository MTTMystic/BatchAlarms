package mttmystic.horus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mttmystic.horus.activities.AlarmActivity
import mttmystic.horus.proto.alarm

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmAction = getString(context, R.string.alarm_action)
        if (intent.action == alarmAction) {
            val appContext : HorusApp = context.getApplicationContext()  as HorusApp
            //var alarmID : Int = intent.getIntExtra("alarm_id", 0)
            //appContext.notificationMgr.cancelNotification(context, alarmID)
            //appContext.alarmRepo.toggleAlarm(alarmID)

            val alarmIDKey = getString(context, R.string.alarm_id_key)
            val alarmTimeKey = getString(context, R.string.alarm_time_key)

            val alarmTime = intent.getStringExtra(alarmTimeKey)
            val alarmID = intent.getIntExtra(alarmIDKey, 0)

            CoroutineScope(Dispatchers.IO).launch {
                appContext.alarmRepoNew.toggleAlarm(alarmID)
            }


            appContext.notificationMgr.generateNotification(context, alarmTime, alarmID)
        }
    }
}

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context, intent: Intent) {
        val appContext : HorusApp = context.getApplicationContext() as HorusApp
        val alarmID : Int = intent.getIntExtra("alarm_id", 0)
        //var alarm_id : Int? = intent?.getIntExtra("alarm_id", Int.MAX_VALUE)

        appContext.notificationMgr.cancelNotification(context, alarmID)


    }
}


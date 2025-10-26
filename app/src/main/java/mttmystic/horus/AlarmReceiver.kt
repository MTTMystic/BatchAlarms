package mttmystic.horus

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getString
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.horus.data.AlarmRepository

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    @Inject lateinit var alarmRepo : AlarmRepository
    @Inject lateinit var notificationMgr : Notifications

    override fun onReceive(@ApplicationContext context: Context, intent: Intent) {
        val alarmAction = getString(context, R.string.alarm_action)


        if (intent.action == alarmAction) {
            val appContext : App = context.applicationContext as App
            //var alarmID : Int = intent.getIntExtra("alarm_id", 0)
            //appContext.notificationMgr.cancelNotification(context, alarmID)
            //appContext.alarmRepo.toggleAlarm(alarmID)

            val alarmIDKey = getString(context, R.string.alarm_id_key)
            val alarmTimeKey = getString(context, R.string.alarm_time_key)

            val alarmTime = intent.getStringExtra(alarmTimeKey)
            val alarmID = intent.getIntExtra(alarmIDKey, 0)

            CoroutineScope(Dispatchers.IO).launch {
                alarmRepo.toggleAlarm(alarmID)
            }


            notificationMgr.generateNotification(context, alarmTime, alarmID)
        }
    }
}

@AndroidEntryPoint
class StopAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var notificationMgr : Notifications

    override fun onReceive(@ApplicationContext context : Context, intent: Intent) {
        val appContext : App = context.getApplicationContext() as App
        val alarmID : Int = intent.getIntExtra("alarm_id", 0)
        //var alarm_id : Int? = intent?.getIntExtra("alarm_id", Int.MAX_VALUE)

        notificationMgr.cancelNotification(context, alarmID)


    }
}


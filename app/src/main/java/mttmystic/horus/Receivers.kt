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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mttmystic.horus.data.AlarmRepository

object ReceiverScope : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.IO + job
}
@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject lateinit var handler : AlarmHandler
    override fun onReceive(@ApplicationContext context: Context, intent: Intent) {
        val alarmAction = getString(context, R.string.alarm_action)


        if (intent.action == alarmAction) {

            val alarmIDKey = getString(context, R.string.alarm_id_key)
            val alarmTimeKey = getString(context, R.string.alarm_time_key)

            val alarmTime = intent.getStringExtra(alarmTimeKey)
            val alarmID = intent.getIntExtra(alarmIDKey, 0)

            val result = goAsync()

            ReceiverScope. launch {
                handler.onAlarm(alarmID, alarmTime)
                result.finish()
            }




        }
    }
}

@AndroidEntryPoint
class StopAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var handler : AlarmHandler
    override fun onReceive(@ApplicationContext context : Context, intent: Intent) {
        val alarmID : Int = intent.getIntExtra("alarm_id", 0)
        //right now async is not strictly necessary because this isn't suspending but jic
        val result = goAsync()
        ReceiverScope.launch {
            handler.stopAlarm(alarmID)
            result.finish()
        }
    }
}

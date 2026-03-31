package mttmystic.batchAlarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.domain.AlarmHandler
import mttmystic.batchAlarms.domain.oldAlarmHandler

object ReceiverScope : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.IO + job
}

object AlarmIntentKeys {
    val idKey = "ALARM_ID"
}


@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject lateinit var alarmHandler : AlarmHandler
    override fun onReceive(@ApplicationContext context: Context, intent: Intent) {
        val result = goAsync()
        val alarmId = intent.getIntExtra(AlarmIntentKeys.idKey, 0)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                alarmHandler.onTrigger(alarmId)
            } finally {
                result.finish()
            }
        }
        /*
        val alarmAction = getString(context, R.string.alarm_action)


        if (intent.action == alarmAction) {

            val alarmIDKey = getString(context, R.string.alarm_id_key)
            val alarmTimeKey = getString(context, R.string.alarm_time_key)

            val alarmTime = intent.getStringExtra(alarmTimeKey)
            val alarmID = intent.getIntExtra(alarmIDKey, 0)
            val alarmHour = intent.getIntExtra("ALARM_HOUR", 0)
            val alarmMinute = intent.getIntExtra("ALARM_HOUR", 0)
            val result = goAsync()

            ReceiverScope. launch {
                handler.onAlarm(alarmID,alarmTime,
                    alarmHour, alarmMinute)
                result.finish()
            }
        }*/
    }
}

@AndroidEntryPoint
class StopAlarmReceiver : BroadcastReceiver() {
    @Inject lateinit var alarmHandler : AlarmHandler
    override fun onReceive(@ApplicationContext context : Context, intent: Intent) {
        val alarmId : Int = intent.getIntExtra("alarm_id", 0)
        //right now async is not strictly necessary because this isn't suspending but jic
        val result = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                alarmHandler.onStop()
            } finally {
                result.finish()
            }
        }
    }
}

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var handler: AlarmHandler

    override fun onReceive(@ApplicationContext context : Context, intent: Intent) {
        val result = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                handler.onInit()
            } finally {
                result.finish()
            }
        }
    }
}
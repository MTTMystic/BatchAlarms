package mttmystic.horus

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import mttmystic.horus.proto.Alarm

class AlarmService @Inject constructor(@ApplicationContext private val context : Context) {
    private val _alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

     fun timeString(hour: Int, minute: Int) : String {
        return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

     fun setAlarm(alarm: Alarm) {
        val alarmAction = ContextCompat.getString(context, R.string.alarm_action)
        val alarmIDKey = ContextCompat.getString(context, R.string.alarm_id_key)
        val alarmTimeKey = ContextCompat.getString(context, R.string.alarm_time_key)
        val timeString = timeString(alarm.hour, alarm.minute)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, timeString)
            putExtra(
                alarmIDKey,
                alarm.id
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.millis,
            pendingIntent
        )
    }

     fun setAlarmsList(alarms : List<Alarm>) {
        alarms.forEach {
                alarm ->
                    if (alarm.active) {
                        setAlarm(alarm)
                    }
        }
    }

     fun cancelAlarm(id: Int, hour: Int, minute: Int) {
        val alarmAction = ContextCompat.getString(context, R.string.alarm_action)
        val alarmIDKey = ContextCompat.getString(context, R.string.alarm_id_key)
        val alarmTimeKey = ContextCompat.getString(context, R.string.alarm_time_key)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, timeString(hour, minute))
            putExtra(
                alarmIDKey,
                id
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.cancel(pendingIntent)
    }

     fun cancelAlarmsList(alarms : List<Alarm>) {
        alarms.forEach { alarm ->
            cancelAlarm(alarm.id, alarm.hour, alarm.minute)
        }
    }

}
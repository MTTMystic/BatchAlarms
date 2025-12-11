package mttmystic.batchAlarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import mttmystic.batchAlarms.data.Interval
import batchAlarms.proto.Alarm
import mttmystic.batchAlarms.data.Span
//import mttmystic.batchAlarms.proto.Alarm

import java.time.LocalTime
import java.time.ZonedDateTime

class AlarmService @Inject constructor(@ApplicationContext private val context: Context) {
    private val _alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun computeNextAlarm(hour: Int, minute:Int, now : ZonedDateTime = ZonedDateTime.now()) : ZonedDateTime {
        val alarmTime = LocalTime.of(hour, minute)

        val todayAtAlarm = now.with(alarmTime)

        val nextAlarm = if (!todayAtAlarm.isAfter(now)) {
            todayAtAlarm.plusDays(1)
        } else {
            todayAtAlarm
        }

        return nextAlarm
    }

    fun timeString(hour: Int, minute: Int): String {
        return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

    fun generateAlarmTimes(span : Span, interval : Interval) : MutableList<Long> {
        val timeList : MutableList<Long> = mutableListOf()
        val firstAlarmMillis = computeNextAlarm(span.start.hour, span.start.minute)
            .toInstant()
            .toEpochMilli()

        val step: Long = interval.inMillis()
        val end: Long = firstAlarmMillis + span.lengthInMillis()
        var curr = firstAlarmMillis
        while (curr <= end) {
            timeList.add(curr)
            curr += step
        }

        if ((curr - step) != end) {
            timeList.add(curr)
        }

        return timeList
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

    fun setAlarmsList(alarms: List<Alarm>) {
        alarms.forEach { alarm ->
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

    fun cancelAlarmsList(alarms: List<Alarm>) {
        alarms.forEach { alarm ->
            cancelAlarm(alarm.id, alarm.hour, alarm.minute)
        }
    }

}
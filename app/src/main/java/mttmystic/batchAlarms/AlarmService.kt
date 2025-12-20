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
import batchAlarms.proto.alarm
import mttmystic.batchAlarms.data.Span
import mttmystic.batchAlarms.R
import mttmystic.batchAlarms.data.AlarmProto
import java.time.Instant
//import mttmystic.batchAlarms.proto.Alarm

import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

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

    fun computeNextAlarmMillis(alarmTime : LocalTime) : Long {
        return computeNextAlarm(alarmTime.hour, alarmTime.minute)
            .toInstant()
            .toEpochMilli()
    }

    fun computeNextAlarmMillis(hour:Int, minute:Int) : Long {
        return computeNextAlarm(hour, minute)
            .toInstant()
            .toEpochMilli()
    }

    fun alarmTimeFromMillis(millis : Long) : Pair<Int, Int> {
        val zonedAlarmTime = Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())

        val hour : Int = zonedAlarmTime.hour
        val min  : Int = zonedAlarmTime.minute

        return Pair(hour, min)
    }

    fun timeString(hour: Int, minute: Int): String {
        return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

    fun generateAlarmsFromSpan(span: Span, interval: Interval) : MutableList<AlarmProto>{
        val alarmList : MutableList<AlarmProto> = mutableListOf()
        //val startMillis = computeNextAlarmMillis(span.start.hour, span.start.minute)
        //val endMillis = computeNextAlarmMillis(span.end.hour, span.end.minute)

        var alarmTime = computeNextAlarm(span.start.hour, span.start.minute)
        val endTime = computeNextAlarm(span.end.hour, span.end.minute)

        while(!alarmTime.isAfter(endTime)){
            val alarmHour = alarmTime.hour
            val alarmMinute = alarmTime.minute
            val alarmMillis = computeNextAlarmMillis(alarmHour, alarmMinute)
            //TODO decouple id from request code - use a hash with id and intent label to generate intent request code
            val alarmId = Random.nextInt()
            val alarm = AlarmProto(
                alarmHour,
                alarmMinute,
                alarmMillis,
                alarmId,
            )
            alarmList.add(alarm)
            alarmTime = alarmTime.plusMinutes(interval.length.toLong())
        }

        //val step : Long = interval.lengthInMillis()

        return alarmList
    }

    fun generateAlarmTimes(span : Span, interval : Interval) : MutableList<Long> {
        val timeList : MutableList<Long> = mutableListOf()
        val startMillis = computeNextAlarmMillis(span.start.hour, span.start.minute)
        val endMillis = computeNextAlarmMillis(span.end.hour, span.end.minute)


        val step: Long = interval.lengthInMillis()
        val end: Long = startMillis + span.lengthInMillis()
        var curr = startMillis
        while (curr <= endMillis) {
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

    //TODO this should check to see if the alarm is in the future or not
    fun setAlarmsList(alarms: List<Alarm>) {
        alarms.forEach { alarm ->
            val futureAlarm = alarm.millis > System.currentTimeMillis()
            if (alarm.active && futureAlarm) {
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
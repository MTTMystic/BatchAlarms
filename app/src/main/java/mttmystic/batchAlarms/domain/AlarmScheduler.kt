package mttmystic.batchAlarms.domain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import mttmystic.batchAlarms.AlarmIntentKeys
import mttmystic.batchAlarms.AlarmReceiver
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZonedDateTime

interface AlarmScheduler {

    fun computeNextAlarmTime(hour:Int,
                             minute: Int,
                             repeatDays : Set<DayOfWeek>,
                             now : ZonedDateTime = ZonedDateTime.now()) : ZonedDateTime
    fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek> = emptySet())

    fun cancelAlarm(id: Int)
}


class AlarmSchedulerImpl @Inject constructor (
    @ApplicationContext private val  context : Context
) : AlarmScheduler {

    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    override fun computeNextAlarmTime(
        hour: Int,
        minute: Int,
        repeatDays : Set<DayOfWeek>,
        now : ZonedDateTime) : ZonedDateTime {
        val localTime = LocalTime.of(hour, minute, 0, 0)
        //val now = ZonedDateTime.now()
        val todayAt = now.with(localTime)
        lateinit var nextTime : ZonedDateTime
        if (repeatDays.isEmpty()) {
            val nextTime = if
                    (!todayAt.isAfter(now)) {
                        todayAt.plusDays(1)
                    } else {
                        todayAt
                    }
            return nextTime
        }

        for (day in (0..7)) {
            val candidate = todayAt.plusDays(day.toLong())
            if (repeatDays.contains(candidate.dayOfWeek) && candidate.isAfter(now)) {
                nextTime = candidate
                break
            }
        }

        return nextTime
    }

    fun getIntent(id : Int) : PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmIntentKeys.idKey, id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        return pendingIntent
    }

    override fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek>) {
        val alarmTime = computeNextAlarmTime(hour, minute, repeatDays)
        val millis = alarmTime.toInstant().toEpochMilli()
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            millis,
            getIntent(id)
        )

    }

    override fun cancelAlarm(id: Int) {
        alarmMgr.cancel(getIntent(id))
    }
}
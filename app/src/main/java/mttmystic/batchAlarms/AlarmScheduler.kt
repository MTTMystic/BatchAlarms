package mttmystic.batchAlarms

import android.content.Context
import androidx.annotation.VisibleForTesting
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.ZonedDateTime

interface AlarmScheduler {

    fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek> = emptySet())

    fun cancelAlarm(id: Int)
}


class AlarmSchedulerImpl @Inject constructor (
    @ApplicationContext private val  context : Context
) : AlarmScheduler {

    @VisibleForTesting
    internal fun computeNextAlarmTime(
        hour: Int,
        minute: Int,
        repeatDays : Set<DayOfWeek>,
        now : ZonedDateTime = ZonedDateTime.now()) : ZonedDateTime {
        val localTime = LocalTime.of(hour, minute, 0, 0)
        //val now = ZonedDateTime.now()
        val todayAt = now.with(localTime)
        lateinit var nextTime : ZonedDateTime
        if (repeatDays.isEmpty()) {
            nextTime = if
                    (!todayAt.isAfter(now)) {
                        todayAt.plusDays(1)
                    } else {
                        todayAt
                    }

        }

        for (day in (0..7)) {
            val candidate = todayAt.plusDays(day.toLong())
            if (repeatDays.contains(candidate.dayOfWeek) && candidate.isAfter(now)) {
                nextTime = candidate
            }
        }

        return nextTime
    }

    override fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek>) {}

    override fun cancelAlarm(id: Int) {}
}
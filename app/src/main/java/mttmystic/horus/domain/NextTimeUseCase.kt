package mttmystic.horus.domain

import jakarta.inject.Inject
import mttmystic.horus.data.Time
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class NextTimeUseCase @Inject constructor() {
    operator fun invoke(hour: Int, minute:Int, now: ZonedDateTime = ZonedDateTime.now()) : String {
        val alarmTime = LocalTime.of(hour, minute)

        var nextAlarm = now.with(alarmTime)

        //one time alarm
        //TODO set up for repeating alarms or deferred alarms
        if (nextAlarm.isBefore(now)) {
            nextAlarm = nextAlarm.plusDays(1)
        }

        val nextAlarmDate = nextAlarm.toLocalDate()
        val delta = ChronoUnit.DAYS.between(now, nextAlarmDate)
        val nextAlarmLabel : String = when (delta) {
            0L -> "Today at"
            1L -> "Tomorrow at"
            else -> "Upcoming" //TODO set up for repeating alarms or deferred alarms
        }

        return nextAlarmLabel
    }
}
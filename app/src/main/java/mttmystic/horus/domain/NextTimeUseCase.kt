package mttmystic.horus.domain

import jakarta.inject.Inject
import mttmystic.horus.data.Time
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun computeNextAlarm(hour: Int, minute:Int, now : ZonedDateTime = ZonedDateTime.now()) : ZonedDateTime {
    val alarmTime = LocalTime.of(hour, minute)

    var todayAtAlarm = now.with(alarmTime)

    val nextAlarm = if (!todayAtAlarm.isAfter(now)) {
        todayAtAlarm.plusDays(1)
    } else {
        todayAtAlarm
    }

    return nextAlarm
}

fun nextLabel(nextAlarm: ZonedDateTime, now : ZonedDateTime = ZonedDateTime.now() ) : String {
    val nextDate = nextAlarm.toLocalDate()
    val today = now.toLocalDate()

    val delta = ChronoUnit.DAYS.between(today, nextDate)

    val nextAlarmLabel : String = when (delta) {
        0L -> "today at"
        1L -> "tomorrow at"
        else -> "Upcoming" //TODO set up for repeating alarms or deferred alarms
    }

    return nextAlarmLabel
}
class NextTimeUseCase @Inject constructor() {
    operator fun invoke(hour: Int, minute:Int) : String {
        return nextLabel(computeNextAlarm(hour, minute))
    }
}
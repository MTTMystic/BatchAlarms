package mttmystic.horus.domain

import jakarta.inject.Inject
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit


class DayLabelUseCase @Inject constructor() {
    operator fun invoke(nextAlarm: ZonedDateTime, now : ZonedDateTime = ZonedDateTime.now() ) : String {
        val nextDate = nextAlarm.toLocalDate()
        val today = now.toLocalDate()

        val delta = ChronoUnit.DAYS.between(today, nextDate)

        val nextAlarmLabel : String = when (delta) {
            0L -> "today at"
            1L -> "tomorrow at"
            else -> nextAlarm.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() } + " at"
        }

        return nextAlarmLabel
    }
}
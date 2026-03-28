package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.domain.AlarmScheduler
import java.time.DayOfWeek
import java.time.ZonedDateTime

class CreateAlarmBatch @Inject constructor (
    private val alarmScheduler: AlarmScheduler,
    private val createSingleAlarm : CreateSingleAlarm
){
    suspend operator fun invoke(
        start : Pair<Int, Int> ,
        end: Pair<Int, Int>,
        repeatDays: Set<DayOfWeek>,
        freq : Int,
        now: ZonedDateTime = ZonedDateTime.now()
    ) {
        var alarmTime = alarmScheduler.computeNextAlarmTime(
            start.first,
            start.second,
            repeatDays,
            now
        )

        val endTime = alarmScheduler.computeNextAlarmTime(
            end.first,
            end.second,
            repeatDays,
            alarmTime
        )

        while(!alarmTime.isAfter(endTime)) {
            createSingleAlarm(alarmTime.hour, alarmTime.minute, repeatDays)
            alarmTime = alarmTime.plusMinutes(freq.toLong())
        }
    }
}
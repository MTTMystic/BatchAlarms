package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import java.time.DayOfWeek

class CreateAlarmBatchUseCase @Inject constructor (
    private val alarmScheduler: AlarmScheduler,
    private val createSingleAlarmUseCase : CreateSingleAlarmUseCase
){
    suspend operator fun invoke(start : Pair<Int, Int> , end: Pair<Int, Int>, repeatDays: Set<DayOfWeek>, freq : Int) {
        var alarmTime = alarmScheduler.computeNextAlarmTime(
            start.first,
            start.second,
            repeatDays
        )

        val endTime = alarmScheduler.computeNextAlarmTime(
            end.first,
            end.second,
            repeatDays,
            alarmTime
        )

        while(!alarmTime.isAfter(endTime)) {
            createSingleAlarmUseCase(alarmTime.hour, alarmTime.minute, repeatDays)
            alarmTime = alarmTime.plusMinutes(freq.toLong())
        }
    }
}
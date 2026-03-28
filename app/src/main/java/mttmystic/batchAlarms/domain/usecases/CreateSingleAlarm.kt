package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import java.time.DayOfWeek

class CreateSingleAlarm @Inject constructor (
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler : AlarmScheduler
){
    suspend operator fun invoke(hour24 : Int, minute : Int, repeatDays: Set<DayOfWeek>) {
        val alarmId = alarmRepository.save(
            Alarm(
                hour = hour24,
                minute = minute,
                repeatDays = repeatDays,
                active = true
            )
        )

        alarmScheduler.scheduleAlarm(alarmId, hour24, minute, repeatDays)
    }
}
package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.AlarmRepository
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span

class CreateAlarmsUseCase @Inject constructor(
    private val alarmRepository : AlarmRepository,
    private val alarmService: AlarmService
) {

    suspend operator fun invoke(span: Span, interval : Interval) {
        val alarmTimes = alarmService.generateAlarmTimes(span, interval)
        alarmRepository.generateAlarmsList(alarmTimes)
        alarmService.setAlarmsList(alarmRepository.alarmsList.first())
    }

}
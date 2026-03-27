package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span

class CreateAlarmsUseCase @Inject constructor(
    private val oldAlarmRepository : oldAlarmRepository,
    private val alarmService: AlarmService
) {

    suspend operator fun invoke(span: Span, interval : Interval) {
        /*
        val alarmTimes = alarmService.generateAlarmTimes(span, interval)
        oldAlarmRepository.generateAlarmsList(alarmTimes)
        alarmService.setAlarmsList(oldAlarmRepository.alarmsList.first())
        */
        val alarmList = alarmService.generateAlarmsFromSpan(span, interval)
        oldAlarmRepository.generateAlarmsList(alarmList)
        alarmService.setAlarmsList(oldAlarmRepository.alarmsList.first())
    }

}
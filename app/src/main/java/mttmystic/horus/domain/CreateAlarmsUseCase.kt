package mttmystic.horus.domain

import kotlinx.coroutines.flow.first
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

class CreateAlarmsUseCase(
    private val alarmRepository : AlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke(span : Span, interval: Interval) {
        val alarms = alarmRepository.alarmsList.first()
        //val activeAlarms = alarms.filter {it.active}
        alarmService.cancelAlarmsList(alarms)
        alarmRepository.generateAlarmsList(span, interval)
        alarmService.setAlarmsList(alarms)
    }
}
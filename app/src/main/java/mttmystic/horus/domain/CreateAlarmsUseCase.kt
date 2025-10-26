package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

class CreateAlarmsUseCase @Inject constructor(
    private val alarmRepository : AlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke(span : Span, interval: Interval) {
        //val activeAlarms = alarms.filter {it.active}
        alarmService.cancelAlarmsList(alarmRepository.alarmsList.first())
        alarmRepository.generateAlarmsList(span, interval)
        alarmService.setAlarmsList(alarmRepository.alarmsList.first())
    }
}
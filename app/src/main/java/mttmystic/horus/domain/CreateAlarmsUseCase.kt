package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.horus.AlarmService
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

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
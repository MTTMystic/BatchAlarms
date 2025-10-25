package mttmystic.horus.domain

import kotlinx.coroutines.flow.first
import mttmystic.horus.data.AlarmRepository

class DeleteAlarmsUseCase (
    private val alarmRepository: AlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke() {
        alarmService.cancelAlarmsList(alarmRepository.alarmsList.first())
        alarmRepository.deleteAllAlarms()
    }
}
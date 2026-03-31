package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler

class DeleteAlarms @Inject constructor(
    private val alarmRepository : AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        alarmRepository.getAlarmsFlow().first().forEach {
            alarmRepository.remove(it.id)
            alarmScheduler.cancelAlarm(it.id)
        }
        //alarmService.cancelAlarmsList(oldAlarmRepository.alarmsList.first())
        //oldAlarmRepository.deleteAllAlarms()
    }
}
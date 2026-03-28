package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.oldAlarmRepository

class DeleteAlarms @Inject constructor(
    private val oldAlarmRepository: oldAlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke() {
        alarmService.cancelAlarmsList(oldAlarmRepository.alarmsList.first())
        oldAlarmRepository.deleteAllAlarms()
    }
}
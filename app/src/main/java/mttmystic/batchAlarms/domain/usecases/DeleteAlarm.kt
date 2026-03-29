package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler

class DeleteAlarm @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarmId: Int) {
        alarmRepository.remove(alarmId)
        alarmScheduler.cancelAlarm(alarmId)
    }
}
package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.oldAlarmRepository

class ToggleAlarmUseCase @Inject constructor(
    private val oldAlarmRepository: oldAlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke(id: Int) {
        val alarm = oldAlarmRepository.alarmsList.first().find {it.id == id}
        if (alarm != null) {
            oldAlarmRepository.toggleAlarm(alarm.id)
            if (alarm.active) {
                alarmService.cancelAlarm(alarm.id, alarm.hour, alarm.minute)
            } else {
                //TODO calculate millis of next alarm, update alarm in repo, set alarm
                //TODO investigate using alarmTime as now reference --
                val alarmTime = alarmService.alarmDateTimeFromMillis(alarm.millis)
                val nextTime = alarmService.computeNextAlarmMillis(alarm.hour, alarm.minute)
                oldAlarmRepository.updateAlarmTime(alarm.id, nextTime)
                alarmService.setAlarm(alarm)
            }
        }
        //TODO what if no alarm with that ID exists?

    }
}
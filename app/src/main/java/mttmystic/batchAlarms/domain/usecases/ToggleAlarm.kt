package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler


class ToggleAlarm @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(alarmId: Int) {
        val alarm = alarmRepository.find(alarmId)
        alarmRepository.updateActive(alarmId, !alarm.active)
        if (alarm.active) {
            //so alarm will be disabled
            alarmScheduler.cancelAlarm(alarmId)
        } else {
            //so alarm will be enabled
            alarmScheduler.scheduleAlarm(alarmId, alarm.hour, alarm.minute, alarm.repeatDays)
        }
    }
}

class oldToggleAlarm @Inject constructor(
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
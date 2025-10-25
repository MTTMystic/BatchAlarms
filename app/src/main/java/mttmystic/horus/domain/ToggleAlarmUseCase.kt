package mttmystic.horus.domain

import kotlinx.coroutines.flow.first
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.proto.Alarm
import mttmystic.horus.proto.alarm

class ToggleAlarmUseCase(
    private val alarmRepository: AlarmRepository,
    private val alarmService: AlarmService
) {
    suspend operator fun invoke(id: Int) {
        val alarm = alarmRepository.alarmsList.first().find {it.id == id}
        if (alarm != null) {
            alarmRepository.toggleAlarm(alarm.id)
            if (alarm.active) {
                alarmService.cancelAlarm(alarm.id, alarm.hour, alarm.minute)
            } else {
                alarmService.setAlarm(alarm)
            }
        }
        //TODO what if no alarm with that ID exists?

    }
}
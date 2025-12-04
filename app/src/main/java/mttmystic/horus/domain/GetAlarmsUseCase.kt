package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.AlarmUI
import mttmystic.horus.proto.Alarm
import java.time.ZonedDateTime

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    operator fun invoke() : Flow<List<AlarmUI>> {
        val alarmList =   alarmRepository.alarmsList
        val uiAlarms = alarmList.map { alarms ->
            var nextTime = ZonedDateTime.now()
            alarms.map { alarm ->
                val nextAlarmTime = computeNextAlarm(alarm.hour, alarm.minute, nextTime)
                nextTime = nextAlarmTime
                AlarmUI(
                    protoAlarm = alarm,
                    nextTimeLabel = nextLabel(nextAlarmTime, ZonedDateTime.now())
                )
            }
        }

        return uiAlarms
    }
}
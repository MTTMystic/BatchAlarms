package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.AlarmRepository
import mttmystic.batchAlarms.data.AlarmUI
import java.time.ZonedDateTime

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmService : AlarmService,
    private val dayLabelUseCase: DayLabelUseCase
) {
    operator fun invoke() : Flow<List<AlarmUI>> {
        val alarmList =   alarmRepository.alarmsList
        val uiAlarms = alarmList.map { alarms ->
            var nextTime = ZonedDateTime.now()
            alarms.map { alarm ->
                val nextAlarmTime = alarmService.computeNextAlarm(alarm.hour, alarm.minute, nextTime)
                nextTime = nextAlarmTime
                AlarmUI(
                    protoAlarm = alarm,
                    nextTimeLabel = dayLabelUseCase(nextAlarmTime, ZonedDateTime.now())
                )
            }
        }

        return uiAlarms
    }
}
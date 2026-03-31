package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.data.AlarmUI
import mttmystic.batchAlarms.data.models.uiAlarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.data.repository.oldSettingsRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import java.time.ZonedDateTime


class GetAlarms @Inject constructor (
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val dayLabeler : DayLabel,
    private val timeString : TimeString
) {
    operator fun invoke() : Flow<List<uiAlarm>> {
        val sortedAlarmsFlow = alarmRepository
            .getAlarmsFlow()
            .map {it.sortedWith(compareBy({ it.hour }, {it.minute}))}

        val uiAlarms = sortedAlarmsFlow.map { alarms ->
            alarms.map { alarm->
                val nextTime =
                    alarmScheduler.computeNextAlarmTime(alarm.hour, alarm.minute, alarm.repeatDays)
                uiAlarm(
                    alarm,
                    dayLabeler(nextTime),
                    timeString(alarm.hour, alarm.minute)
                )
            }
        }

        return uiAlarms
    }
}

class oldGetAlarms @Inject constructor(
    private val oldAlarmRepository: oldAlarmRepository,
    private val alarmService : AlarmService,
    private val dayLabel: DayLabel,
    private val oldSettingsRepository: oldSettingsRepository,
    private val timeString : TimeString
) {
    operator fun invoke() : Flow<List<AlarmUI>> {
        val alarmList =   oldAlarmRepository.alarmsList

        val uiAlarms = alarmList.map { alarms ->
            val now = ZonedDateTime.now()
            val sortedByMillis = alarms.sortedBy { alarm ->
                //TODO decide on which sorting logic I want to use!
                alarmService.alarmDateTimeFromMillis(alarm.millis)
            }
            val sortedByTime = alarms.sortedWith(compareBy({ it.hour }, { it.minute }))
            sortedByTime.map { alarm ->

                val nextAlarmTime = alarmService.alarmDateTimeFromMillis(alarm.millis)
                AlarmUI(
                    protoAlarm = alarm,
                    nextTimeLabel = dayLabel(nextAlarmTime, ZonedDateTime.now()),
                    timeString = timeString(alarm.hour, alarm.minute)
                )
            }
        }


        /*
            alarms.sortedWith(
                compareBy<Alarm> {
                    val next = alarmService.computeNextAlarm(it.hour, it.minute)
                    ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDate(), next.toLocalDate())
                }
                    .thenBy {it.hour}
                    .thenBy {it.minute}
            )
        */

        return uiAlarms
    }

}
package mttmystic.batchAlarms.domain

import android.util.Log
import batchAlarms.proto.Alarm
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.AlarmProto
import mttmystic.batchAlarms.data.AlarmRepository
import mttmystic.batchAlarms.data.AlarmUI
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmService : AlarmService,
    private val dayLabelUseCase: DayLabelUseCase
) {
    operator fun invoke() : Flow<List<AlarmUI>> {
        val alarmList =   alarmRepository.alarmsList

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
                    nextTimeLabel = dayLabelUseCase(nextAlarmTime, ZonedDateTime.now())
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
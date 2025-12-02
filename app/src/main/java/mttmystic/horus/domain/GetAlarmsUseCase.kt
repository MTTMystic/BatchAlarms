package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.AlarmUI
import mttmystic.horus.proto.Alarm

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val nextTimeUseCase: NextTimeUseCase
) {
    operator fun invoke() : StateFlow<List<Alarm>> {
        return  alarmRepository.alarmsList
        /*val uiAlarms = alarmList.map { alarms ->
            alarms.map { alarm ->
                AlarmUI(
                    protoAlarm = alarm,
                    nextTimeLabel = nextTimeUseCase(alarm.hour, alarm.minute)
                )
            }
        }

        return uiAlarms*/
    }
}
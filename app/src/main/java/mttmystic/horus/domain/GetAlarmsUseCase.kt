package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.proto.Alarm

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke() : StateFlow<List<Alarm>> {
        return alarmRepository.alarmsList
    }
}
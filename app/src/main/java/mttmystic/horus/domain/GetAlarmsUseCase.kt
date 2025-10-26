package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.proto.Alarm

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    operator fun invoke() : Flow<List<Alarm>> {
        return alarmRepository.alarmsList
    }
}
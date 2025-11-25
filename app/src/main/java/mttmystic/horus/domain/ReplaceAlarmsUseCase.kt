package mttmystic.horus.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.horus.AlarmService
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span

class ReplaceAlarmsUseCase @Inject constructor(
    private val deleteAlarmsUseCase: DeleteAlarmsUseCase,
    private val createAlarmsUseCase: CreateAlarmsUseCase
) {
    suspend operator fun invoke(span : Span, interval: Interval) {
        //val activeAlarms = alarms.filter {it.active}
        deleteAlarmsUseCase()
        createAlarmsUseCase(span, interval)
    }
}
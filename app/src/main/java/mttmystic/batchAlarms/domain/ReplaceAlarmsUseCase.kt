package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span

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
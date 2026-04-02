package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span

/*
class ReplaceAlarms @Inject constructor(
    private val deleteAlarms: DeleteAlarms,
    private val createAlarms: CreateAlarms
) {
    suspend operator fun invoke(span : Span, interval: Interval) {
        //val activeAlarms = alarms.filter {it.active}
        deleteAlarms()
        createAlarms(span, interval)
    }
}*/
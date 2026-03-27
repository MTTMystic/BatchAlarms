package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import java.time.DayOfWeek

class CreateAlarmBatchUseCase @Inject constructor (
   private val createSingleAlarmUseCase : CreateSingleAlarmUseCase
){
    suspend operator fun invoke(start : Pair<Int, Int> , end: Pair<Int, Int>, repeatDays: Set<DayOfWeek>) {

    }
}
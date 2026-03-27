package mttmystic.batchAlarms

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.domain.oldAlarmHandler

class AppInit @Inject constructor (
    private val oldAlarmRepository: oldAlarmRepository,
    private val alarmService: AlarmService,
    private val oldAlarmHandler: oldAlarmHandler
) {
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
             oldAlarmHandler.onInit()
        }

    }
}
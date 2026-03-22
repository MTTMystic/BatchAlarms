package mttmystic.batchAlarms

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.repository.oldAlarmRepository

class AppInit @Inject constructor (
    private val oldAlarmRepository: oldAlarmRepository,
    private val alarmService: AlarmService,
    private val alarmHandler: AlarmHandler
) {
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
             alarmHandler.onInit()
        }
    }
}
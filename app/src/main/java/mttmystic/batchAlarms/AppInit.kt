package mttmystic.batchAlarms

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.local.AlarmDatabase
import mttmystic.batchAlarms.data.repository.oldAlarmRepository

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
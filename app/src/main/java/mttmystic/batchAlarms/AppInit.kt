package mttmystic.batchAlarms

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.AlarmRepository

class AppInit @Inject constructor (
    private val alarmRepository: AlarmRepository,
    private val alarmService: AlarmService,
    private val alarmHandler: AlarmHandler
) {
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
             alarmHandler.onInit()
        }
    }
}
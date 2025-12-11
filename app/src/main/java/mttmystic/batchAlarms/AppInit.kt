package mttmystic.batchAlarms

import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.AlarmRepository

class AppInit @Inject constructor (
    private val alarmRepository: AlarmRepository,
    private val alarmService: AlarmService
) {
    fun init() {
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.alarmsList.collect {
                alarms -> alarmService.setAlarmsList(alarms)
            }
        }
    }
}
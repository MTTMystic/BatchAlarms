package mttmystic.batchAlarms

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.domain.TimeStringUseCase


class AlarmHandler @Inject constructor (
    private val notificationMgr : Notifications,
    private val alarmRepo : oldAlarmRepository,
    private val alarmService : AlarmService,
    private val timeStringUseCase: TimeStringUseCase,
    @ApplicationContext private val appContext : Context
) {
    suspend fun onAlarm(alarmID: Int, alarmTime: String?, alarmHour: Int, alarmMinute: Int) {
        alarmRepo.disableAlarm(alarmID)
        val timeString = timeStringUseCase(alarmHour, alarmMinute)
        notificationMgr.generateNotification(appContext, alarmTime, alarmID)

        //TODO calculate the next time the alarm should fire and update millis in the repo/datastore
    }

    //for now suspend is not needed but jic
    suspend fun stopAlarm(alarmID: Int) {
        notificationMgr.cancelNotification(appContext, alarmID)
    }

    suspend fun onBoot() {
        alarmService.setAlarmsList(alarmRepo.alarmsList.first())
    }

    suspend fun onInit() {
        Log.d("AlarmHandler", "onInit started")
        alarmRepo.alarmsList
            .first { it.isNotEmpty() }
            .forEach {
                Log.d("AlarmHandler", "Processing alarm ${it.hour}:${it.minute}, millis: ${it.millis}, now: ${System.currentTimeMillis()}")
                if (it.millis < System.currentTimeMillis()) {
                    Log.d("AlarmHandler", "Canceling past alarm ${it.hour}:${it.minute}")
                    alarmService.cancelAlarm(it.id, it.hour, it.minute)
                    //stopAlarm(it.id)
                    alarmRepo.disableAlarm(it.id)
                } else {
                    Log.d("AlarmHandler", "Setting future alarm ${it.hour}:${it.minute}")
                    alarmService.setAlarm(it)
                }
        }
    }
}
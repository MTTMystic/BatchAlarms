package mttmystic.batchAlarms

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.data.AlarmRepository


class AlarmHandler @Inject constructor (
    private val notificationMgr : Notifications,
    private val alarmRepo : AlarmRepository,
    private val alarmService : AlarmService,
    @ApplicationContext private val appContext : Context
) {
    suspend fun onAlarm(alarmID: Int, alarmTime: String?) {
        alarmRepo.toggleAlarm(alarmID)
        notificationMgr.generateNotification(appContext, alarmTime, alarmID)
    }

    //for now suspend is not needed but jic
    suspend fun stopAlarm(alarmID: Int) {
        notificationMgr.cancelNotification(appContext, alarmID)
    }

    suspend fun onBoot() {
        alarmService.setAlarmsList(alarmRepo.alarmsList.first())
    }
}
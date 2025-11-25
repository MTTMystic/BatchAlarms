package mttmystic.horus

import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import mttmystic.horus.data.AlarmRepository


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
}
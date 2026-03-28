package mttmystic.batchAlarms.domain

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.AlarmService
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.data.repository.oldAlarmRepository
import mttmystic.batchAlarms.domain.usecases.TimeString
import java.time.LocalTime
import java.time.ZonedDateTime

interface AlarmHandler {
    suspend fun onTrigger(alarmId: Int)

    suspend fun onStop()

    suspend fun onInit(now : ZonedDateTime = ZonedDateTime.now() )
}

class AlarmHandlerImpl @Inject constructor (
    private val notificationHandler: NotificationHandler,
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val timeString:TimeString
) : AlarmHandler {
    override suspend fun onTrigger(alarmId : Int ) {
        //Log.d("AlarmHandler", "alarm fired")
        //get the alarm with given id
        val alarm = alarmRepository.find(alarmId)
        //TODO handle the case where there is no alarm found by that ID
        //get a formatted hh:mm string
        val formattedTimeString = timeString(alarm.hour, alarm.minute)
        //show a notification
        notificationHandler.showNotification(formattedTimeString)
        //disactivate the alarm if it does not repeat
        if (alarm.repeatDays.isEmpty()) {
            alarmRepository.updateActive(alarm.id, false)
        } else {
            //schedule the next alarm (repeating)
            alarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, alarm.repeatDays)
        }
    }

    override suspend fun onStop() {
        //Log.d("AlarmHandler", "alarm stopped")
        notificationHandler.cancelNotification()
    }

    override suspend fun onInit(now: ZonedDateTime) {
        //Log.d("AlarmHandler", "initialization")
        alarmRepository.getAlarmsFlow().first{it.isNotEmpty()}.forEach {
            //only operate on active alarms
            if(it.active) {
                //calculate the time the alarm would have fired
                val alarmTime = LocalTime.of(it.hour, it.minute)
                val todayAt = now.with(alarmTime)
                //did we miss this alarm today?
                val missedAlarm = !now.isBefore(todayAt)
                val shouldReschedule = !missedAlarm or (missedAlarm and it.repeatDays.isNotEmpty())
                if (shouldReschedule) {
                    alarmScheduler.scheduleAlarm(it.id, it.hour, it.minute, it.repeatDays)
                } else if (missedAlarm and it.repeatDays.isEmpty()) {
                    //otherwise the alarm is a one-off alarm that was missed, so disable it
                    alarmScheduler.cancelAlarm(it.id)
                    alarmRepository.updateActive(it.id, false)
                }
            }
        }
    }
}

class oldAlarmHandler @Inject constructor (
    private val notificationMgr : oldNotificationHandler,
    private val alarmRepo : oldAlarmRepository,
    private val alarmService : AlarmService,
    private val timeString: TimeString,
    @ApplicationContext private val appContext : Context
) {
    suspend fun onAlarm(alarmID: Int, alarmTime: String?, alarmHour: Int, alarmMinute: Int) {
        alarmRepo.disableAlarm(alarmID)
        val timeString = timeString(alarmHour, alarmMinute)
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
        Log.d("oldAlarmHandler", "onInit started")
        alarmRepo.alarmsList
            .first { it.isNotEmpty() }
            .forEach {
                Log.d("oldAlarmHandler", "Processing alarm ${it.hour}:${it.minute}, millis: ${it.millis}, now: ${System.currentTimeMillis()}")
                if (it.millis < System.currentTimeMillis()) {
                    Log.d("oldAlarmHandler", "Canceling past alarm ${it.hour}:${it.minute}")
                    alarmService.cancelAlarm(it.id, it.hour, it.minute)
                    //stopAlarm(it.id)
                    alarmRepo.disableAlarm(it.id)
                } else {
                    Log.d("oldAlarmHandler", "Setting future alarm ${it.hour}:${it.minute}")
                    alarmService.setAlarm(it)
                }
        }
    }
}
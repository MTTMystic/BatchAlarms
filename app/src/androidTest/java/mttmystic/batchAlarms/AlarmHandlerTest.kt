package mttmystic.batchAlarms

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmHandlerImpl
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.NotificationHandler
import mttmystic.batchAlarms.domain.usecases.TimeStringUseCase
import org.junit.Test
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime

class AlarmHandlerTest {
    val mockAlarmRepository = mockk<AlarmRepository>(relaxed = true)
    val mockNotificationHandler = mockk<NotificationHandler>(relaxed = true)
    val mockAlarmScheduler = mockk<AlarmScheduler>(relaxed = true)
    val mockTimeStringUseCase = mockk<TimeStringUseCase>()

    val now = ZonedDateTime.of(2026, 3, 24, 5, 0, 0, 0, ZoneId.systemDefault())

    val handler = AlarmHandlerImpl(
        mockNotificationHandler,
        alarmRepository = mockAlarmRepository,
        alarmScheduler = mockAlarmScheduler,
        timeStringUseCase = mockTimeStringUseCase
    )

    val alarm = Alarm(1, 6, 0, emptySet(), true)
    val timeString = "06:00"
    val repeatDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    @Test
    fun `onTrigger fires notification and disables non-repeating alarm` () = runBlocking {
        coEvery {mockAlarmRepository.find(1)} returns alarm
        coEvery {mockTimeStringUseCase(alarm.hour, alarm.minute)} returns timeString

        handler.onTrigger(1)

        coVerify {mockNotificationHandler.showNotification(timeString)}
        //coVerify { mockAlarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, alarm.repeatDays) }
        coVerify { mockAlarmRepository.updateActive(alarm.id, false) }

    }

    @Test
    fun `onTrigger fires notification and reschedules repeating alarm` () = runBlocking {
        coEvery { mockAlarmRepository.find(1) } returns alarm.copy(repeatDays=repeatDays)
        coEvery {mockTimeStringUseCase(alarm.hour, alarm.minute)} returns timeString

        handler.onTrigger(1)

        coVerify { mockNotificationHandler.showNotification(timeString) }
        coVerify { mockAlarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, repeatDays) }

    }

    @Test
    fun `onStop cancels notification` () = runBlocking {
        handler.onStop()
        coVerify { mockNotificationHandler.cancelNotification() }
    }

    @Test
    fun `onInit only reschedules active alarm given valid active one-off alarm` () = runBlocking {
        val alarmList = listOf(alarm, alarm.copy(id = 2, active = false))
        coEvery {mockAlarmRepository.getAlarmsFlow()} returns flowOf(alarmList)
        handler.onInit(now)
        coVerify(exactly = 1) { mockAlarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, emptySet()) }
    }

    @Test
    fun `onInit only reschedules active alarm given valid repeat alarm` () = runBlocking {
        val alarmList = listOf(alarm.copy(repeatDays = repeatDays), alarm.copy(id = 2, active = false))
        coEvery {mockAlarmRepository.getAlarmsFlow()} returns flowOf(alarmList)
        handler.onInit(now)
        coVerify(exactly = 1) { mockAlarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, repeatDays) }
    }

    @Test
    fun `onInit disables missed one-off alarm` () = runBlocking {
        val alarmList = listOf(alarm.copy(hour = 4))
        coEvery { mockAlarmRepository.getAlarmsFlow() } returns flowOf(alarmList)
        handler.onInit(now)
        coVerify { mockAlarmRepository.updateActive(alarm.id, false) }
    }

    @Test
    fun `onInit reschedules missed repeating alarm` () = runBlocking {
        val alarmList = listOf(alarm.copy(hour = 4, repeatDays = repeatDays))
        coEvery { mockAlarmRepository.getAlarmsFlow() } returns flowOf(alarmList)
        handler.onInit(now)
        mockAlarmScheduler.scheduleAlarm(alarm.id, alarm.hour, alarm.minute, repeatDays)
    }

}
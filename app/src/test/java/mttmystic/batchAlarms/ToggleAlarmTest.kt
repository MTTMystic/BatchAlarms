package mttmystic.batchAlarms

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.usecases.ToggleAlarms
import org.junit.jupiter.api.Test

class ToggleAlarmTest {
    val mockAlarmRepository : AlarmRepository = mockk<AlarmRepository>(relaxed=true)
    val mockAlarmScheduler : AlarmScheduler = mockk<AlarmScheduler>(relaxed = true)
    val toggleAlarmUseCase = ToggleAlarms(mockAlarmRepository, mockAlarmScheduler)
    val alarm = Alarm(hour=0, minute =0, repeatDays = emptySet(), active=true)

    @Test
    fun `toggle active alarm disables and cancels alarm` () = runBlocking {
        coEvery {mockAlarmRepository.find(0)} returns alarm
        toggleAlarmUseCase(0)
        coVerify {mockAlarmScheduler.cancelAlarm(0)}
        coVerify {mockAlarmRepository.updateActive(0, false)}
    }

    @Test
    fun `toggle inactive alarm schedules alarm` () = runBlocking {
        coEvery {mockAlarmRepository.find(0)} returns alarm.copy(active = false)
        toggleAlarmUseCase(0)
        coVerify {mockAlarmScheduler.scheduleAlarm(0, alarm.hour, alarm.minute, alarm.repeatDays)}
        coVerify{mockAlarmRepository.updateActive(0, true)}
    }
}
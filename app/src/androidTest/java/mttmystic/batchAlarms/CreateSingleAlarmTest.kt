package mttmystic.batchAlarms

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.usecases.CreateSingleAlarm
import org.junit.Test

class CreateSingleAlarmTest {
    val mockAlarmRepository = mockk<AlarmRepository>(relaxed = true)
    val mockAlarmScheduler = mockk<AlarmScheduler>(relaxed = true)
    val singleAlarmUseCase : CreateSingleAlarm = CreateSingleAlarm(mockAlarmRepository, mockAlarmScheduler)
    val expectedAlarm = Alarm(hour = 5, minute = 0, repeatDays = emptySet(), active = true)
    @Test
    fun `create single alarm saves correct alarm to repo` () = runBlocking {
        singleAlarmUseCase(expectedAlarm.hour, expectedAlarm.minute, emptySet())
        coVerify { mockAlarmRepository.save(expectedAlarm) }
    }

    @Test
    fun `create single alarm schedules alarm` () = runBlocking {
        singleAlarmUseCase(expectedAlarm.hour, expectedAlarm.minute, emptySet())
        //coEvery {mockAlarmRepository.save(any<Alarm>())}  returns 1
        coVerify { mockAlarmScheduler.scheduleAlarm(0, expectedAlarm.hour, expectedAlarm.minute, emptySet()) }
    }
}
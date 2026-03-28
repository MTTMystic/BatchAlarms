package mttmystic.batchAlarms

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.usecases.CreateAlarmBatchUseCase
import mttmystic.batchAlarms.domain.usecases.CreateSingleAlarmUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AlarmBatchUseCaseTest {
    val now = ZonedDateTime.of(2026, 3, 24, 5, 0, 0, 0, ZoneId.systemDefault())
    val mockAlarmRepository = mockk<AlarmRepository>(relaxed = true)
    val mockAlarmScheduler = mockk<AlarmScheduler>(relaxed=true)
    val singleAlarmUseCase : CreateSingleAlarmUseCase = CreateSingleAlarmUseCase(mockAlarmRepository, mockAlarmScheduler)
    val alarmBatchUseCase : CreateAlarmBatchUseCase = CreateAlarmBatchUseCase(mockAlarmScheduler, singleAlarmUseCase)
    val expectedAlarm = Alarm(hour = 6, minute = 0, repeatDays = emptySet(), active = true)
    val start = Pair(expectedAlarm.hour, expectedAlarm.minute)
    val end = Pair(7, 0)
    val freq = 30

    val expectedAlarmTime = now.with(LocalTime.of(expectedAlarm.hour, expectedAlarm.minute))
    val expectedEndTime = now.with(LocalTime.of(end.first, end.second))
    @Before
    fun setup() {
        coEvery {mockAlarmRepository.save(any<Alarm>())} returns 0
        every { mockAlarmScheduler.computeNextAlarmTime(expectedAlarm.hour, expectedAlarm.minute, emptySet(), now) } returns expectedAlarmTime
        every { mockAlarmScheduler.computeNextAlarmTime(end.first, end.second, emptySet(), expectedAlarmTime) } returns expectedEndTime
    }

    @Test
    fun `create alarm batch schedules alarms at correct times given freq` () = runBlocking {
        alarmBatchUseCase(start, end, emptySet(), freq, now)
        coVerify(exactly = 1) {singleAlarmUseCase(expectedAlarm.hour, expectedAlarm.minute, emptySet())}
        coVerify(exactly = 1) {singleAlarmUseCase(expectedAlarm.hour, expectedAlarm.minute + freq, emptySet())}
        coVerify(exactly = 1) {singleAlarmUseCase(expectedAlarm.hour+1, 0, emptySet())}
    }
}
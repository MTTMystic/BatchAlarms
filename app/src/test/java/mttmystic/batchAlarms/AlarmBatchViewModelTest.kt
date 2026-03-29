package mttmystic.batchAlarms

import io.mockk.mockk
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.usecases.CreateAlarmBatch
import mttmystic.batchAlarms.domain.usecases.CreateSingleAlarm
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmBatchViewModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek

class AlarmBatchViewModelTest {
    val mockAlarmRepository = mockk<AlarmRepository>(relaxed = true)
    val mockAlarmScheduler = mockk<AlarmScheduler>(relaxed = true)
    val createSingleAlarm : CreateSingleAlarm = CreateSingleAlarm(mockAlarmRepository, mockAlarmScheduler)
    val createAlarmBatch = CreateAlarmBatch(mockAlarmScheduler, createSingleAlarm)
    val alarmBatchViewModel : CreateAlarmBatchViewModel = CreateAlarmBatchViewModel(createAlarmBatch)

    @Test
    fun `empty freq text returns valid` () {
        assert(alarmBatchViewModel.validateFreqInput(""))
    }

    @Test
    fun `freq text containing non-digit returns invalid` () {
        assert(!alarmBatchViewModel.validateFreqInput("4a"))
    }

    @Test
    fun `freq text containing only digits from 5 to 60 returns valid` () {
        assert(alarmBatchViewModel.validateFreqInput("40"))
    }

    @Test
    fun `freq text containing digits below 5 returns invalid` () {
        assert(!alarmBatchViewModel.validateFreqInput("04"))
    }

    @Test
    fun `freq text containing digits above 60 returns invalid` () {
        assert(!alarmBatchViewModel.validateFreqInput("61"))
    }

    @Test
    fun `toggle repeat days with new repeat day adds day` () {
        alarmBatchViewModel.toggleRepeatDay(DayOfWeek.TUESDAY.toString())
        assertEquals(alarmBatchViewModel.repeatDays.value, setOf(DayOfWeek.TUESDAY))
    }

    @Test
    fun `toggle repeat days with existing repeat day removes day` () {
        alarmBatchViewModel.toggleRepeatDay(DayOfWeek.TUESDAY.toString())
        alarmBatchViewModel.toggleRepeatDay(DayOfWeek.TUESDAY.toString())
        assertEquals(alarmBatchViewModel.repeatDays.value, emptySet<DayOfWeek>())
    }
}
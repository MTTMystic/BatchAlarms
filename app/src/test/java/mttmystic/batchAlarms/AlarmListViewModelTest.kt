@file:OptIn(ExperimentalCoroutinesApi::class)

package mttmystic.batchAlarms

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mttmystic.batchAlarms.data.models.Alarm
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.usecases.DayLabel
import mttmystic.batchAlarms.domain.usecases.DeleteAlarms
import mttmystic.batchAlarms.domain.usecases.GetAlarms
import mttmystic.batchAlarms.domain.usecases.TimeString
import mttmystic.batchAlarms.domain.usecases.ToggleAlarms
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.collections.emptySet

class AlarmListViewModelTest {
    val mockRepository = mockk<AlarmRepository>(relaxed = true)
    val mockScheduler = mockk<AlarmScheduler>(relaxed = true)
    val mockDayLabeler = mockk<DayLabel>(relaxed = true)
    val mockTimeString = mockk<TimeString>(relaxed = true)
    val toggleAlarmUseCase = ToggleAlarms(mockRepository, mockScheduler)
    val oldGetAlarmsUseCase = GetAlarms(
        mockRepository,
        mockScheduler,
        mockDayLabeler,
        mockTimeString
    )
    val deleteAlarmsUseCase = DeleteAlarms(mockRepository, mockScheduler)

    lateinit var mockAlarmListViewModel : AlarmListViewModel

    val alarm = Alarm(
        id = 0,
        hour = 5,
        minute = 0,
        repeatDays = emptySet(),
        active = true
    )

    val oldGetAlarms = setOf(alarm, alarm.copy(id = 1, hour = 6), alarm.copy(id = 2, hour = 7))

    @BeforeEach
    fun setup() {

        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockAlarmListViewModel = AlarmListViewModel(
            toggleAlarmUseCase,
            getOldGetAlarmsUseCase,
            deleteAlarmsUseCase
        )
        coEvery {mockRepository.find(0)} returns alarm
        coEvery {mockRepository.find(1)} returns oldGetAlarms.elementAt(1).copy(active = false)
        coEvery {mockRepository.find(2)} returns oldGetAlarms.elementAt(2)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("SelectionTest")
    inner class SelectionTest {
        @Test
        fun `long press adds alarm id to selectedIds`() = runBlocking {
            mockAlarmListViewModel.onAlarmLongPress(0)
            assert(mockAlarmListViewModel.selectedIds.value.contains(0))
        }

        @Test
        fun `clicking an alarm that is already selected de-selects it`() = runBlocking {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(0)
            assert(!mockAlarmListViewModel.selectedIds.value.contains(0))
        }


        @Test
        fun `clicking an alarm that is not already selected selects it`() = runBlocking {
            mockAlarmListViewModel.onAlarmSelectionClick(1)
            assert(mockAlarmListViewModel.selectedIds.value.contains(1))
        }

        @Test
        fun `clearing all selected alarms empties the list`() = runBlocking {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(1)
            mockAlarmListViewModel.clearSelected()
            assertEquals(emptySet<Int>(), mockAlarmListViewModel.selectedIds.value)
        }

        @Test
        fun `select all selects all alarms` () = runTest {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(1)
            mockAlarmListViewModel.selectAll()
            assertEquals(setOf(0,1), mockAlarmListViewModel.selectedIds.value)
        }
    }

    @Nested
    @DisplayName("ToggleTest")
    inner class ToggleTest{
        @BeforeEach
        fun setUp() {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(1)
            mockAlarmListViewModel.onAlarmSelectionClick(2)
        }

        @Test
        fun `toggle selected activates all alarms` () = runTest {
            /*
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmClick(1)
            mockAlarmListViewModel.onAlarmClick(2)*/
            mockAlarmListViewModel.toggleSelected(true)
            advanceUntilIdle()
            //coVerify { toggleAlarmUseCase.invoke(mockAlarmListViewModel.selectedIds.value, true) }
            //alarm 0 should still be active
            coVerify {mockRepository.updateActive(0, true)}
            coVerify {mockRepository.updateActive(1, true)}
            coVerify {mockRepository.updateActive(2, true)}
        }

        @Test
        fun `toggle selected cancels all alarms` () = runTest {
            /*mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmClick(1)
            mockAlarmListViewModel.onAlarmClick(2)*/
            mockAlarmListViewModel.toggleSelected(false)
            advanceUntilIdle()
            //coVerify { toggleAlarmUseCase.invoke(mockAlarmListViewModel.selectedIds.value, true) }
            //alarm 0 should still be active
            coVerify {mockRepository.updateActive(0, false)}
            coVerify {mockRepository.updateActive(1, false)}
            coVerify {mockRepository.updateActive(2, false)}
        }
    }

    @Nested
    @DisplayName("DeleteTest")
    inner class DeleteTest{
        @Test
        fun `delete selected alarms deletes the selected alarms` () = runTest {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(2)
            assertEquals(setOf<Int>(0, 2), mockAlarmListViewModel.selectedIds.value)
            mockAlarmListViewModel.deleteSelected()
            advanceUntilIdle()
            assertEquals(emptySet<Int>(), mockAlarmListViewModel.selectedIds.value)
            coVerify { mockRepository.remove(0) }
            coVerify { mockRepository.remove(2) }
            coVerify { mockScheduler.cancelAlarm(0) }
            coVerify {mockScheduler.cancelAlarm(2)}
        }

        @Test
        fun `delete alarm deletes correct alarm` () = runTest {
            mockAlarmListViewModel.onAlarmLongPress(0)
            mockAlarmListViewModel.onAlarmSelectionClick(1)
            mockAlarmListViewModel.deleteAlarm(0)
            advanceUntilIdle()
            assertEquals(setOf<Int>(1), mockAlarmListViewModel.selectedIds.value)
        }
    }

}
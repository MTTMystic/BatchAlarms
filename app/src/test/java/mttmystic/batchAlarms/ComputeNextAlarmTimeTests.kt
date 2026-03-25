package mttmystic.batchAlarms

import android.app.AlarmManager
import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

class ComputeNextAlarmTimeTests {
    val now = ZonedDateTime.of(2026, 3, 24, 5, 0, 0, 0, ZoneId.systemDefault())
    val mockContext = mockk<Context>(relaxed = true)
    val mockAlarmManager = mockk<AlarmManager>(relaxed = true).also {
        every {mockContext.getSystemService(Context.ALARM_SERVICE)} returns it
    }

    val repeatDays = setOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    val scheduler = AlarmSchedulerImpl(mockContext)

    @Test
    fun `returns today for non-repeat alarm for later today` () {
        val correct = now.plusHours(1)
        val result = scheduler.computeNextAlarmTime(now.hour +1, 0, emptySet(), now)
        assertEquals(correct, result)
    }

    @Test
    fun `returns tomorrow for non-repeat alarm when hour-min has passed today` () {
        val correct = now.plusDays(1).minusHours(1)
        val result = scheduler.computeNextAlarmTime(now.hour - 1, 0, emptySet(), now)
        assertEquals(correct, result)
    }

    @Test
    fun `returns tomorrow for non-repeat alarm when hour-min is now` () {
        val correct = now.plusDays(1)
        val result = scheduler.computeNextAlarmTime(now.hour, 0, emptySet(), now)
        assertEquals(correct, result)
    }

    @Test
    fun `returns next repeat day for repeat alarm when today in repeat days and hour-min is now` () {
        val correct = now.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))
        val result = scheduler.computeNextAlarmTime(now.hour, 0, repeatDays, now)
        assertEquals(correct, result)
    }

    @Test
    fun `returns today for repeating alarm for later today if today in repeatDays` () {
        val correct = now.plusHours(1)
        val result = scheduler.computeNextAlarmTime(now.hour+1, now.minute, repeatDays, now)
        assertEquals(correct, result)
    }

    @Test fun `returns next repeat day if today is not in repeatDays but hour-min has not passed` () {
        val futureNow = now.with(TemporalAdjusters.next(DayOfWeek.THURSDAY))
        val correct = now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).minusHours(1)
        val result = scheduler.computeNextAlarmTime(now.hour-1, now.minute, repeatDays, futureNow)
        assertEquals(correct, result)

    }

    @Test
    fun `returns next repeat day if hour-min has passed today` () {
        val futureNow = now.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY))
        val correct = now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).minusHours(1)
        val result = scheduler.computeNextAlarmTime(now.hour - 1, now.minute, repeatDays, futureNow)
        assertEquals(correct, result)
    }
}
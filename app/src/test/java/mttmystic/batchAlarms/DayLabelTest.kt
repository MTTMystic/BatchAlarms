package mttmystic.batchAlarms

import mttmystic.batchAlarms.domain.usecases.DayLabel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

class DayLabelTest {
    val now = ZonedDateTime.of(2026, 3, 24, 5, 0, 0, 0, ZoneId.systemDefault())

    @Test
    fun `returns today when alarm is later today`() {
        val nextTime = now.plusHours(1)
        val result = DayLabel()(nextTime, now)
        val correct = "today at"
        assertEquals(correct, result)
    }

    @Test
    fun `returns tommorow when alarm is tomorrow`() {
        val nextTime = now.plusDays(1)
        val result = DayLabel()(nextTime, now)
        val correct = "tomorrow at"
        assertEquals(correct, result)
    }

    @Test
    fun `returns correct day of week when alarm is beyond tomorrow`() {
        val nextTime = now.with(TemporalAdjusters.next(DayOfWeek.THURSDAY))
        val result = DayLabel()(nextTime, now)
        val correct = "Thursday at"
        assertEquals(correct, result)
    }

}
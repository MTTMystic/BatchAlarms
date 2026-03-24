package mttmystic.batchAlarms

import junit.framework.TestCase.assertEquals
import mttmystic.batchAlarms.data.local.Converters
import org.junit.Test
import java.time.DayOfWeek

class TypeConverterTest {
    val converters = Converters()
    val dayString = "TUESDAY,WEDNESDAY,FRIDAY"
    val daySet = setOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    @Test
    fun `string of days is converted to a DayOfWeek set` () {
        val result = converters.setToString(daySet)
        assertEquals(dayString, result)
    }

    @Test
    fun `DayOfWeek set is converted to a string of days` () {
        val result = converters.stringToSet(dayString)
        assertEquals(daySet, result)
    }
}
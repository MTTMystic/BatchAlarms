package mttmystic.batchAlarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import io.mockk.verify
import mttmystic.batchAlarms.domain.AlarmSchedulerImpl
import org.junit.Test
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime

class ScheduleAlarmTest {
    val now = ZonedDateTime.of(2026, 3, 24, 5, 0, 0, 0, ZoneId.systemDefault())
    //val mockContext = spyk(InstrumentationRegistry.getInstrumentation().targetContext)
    val mockAlarmManager = mockk<AlarmManager>(relaxed = true)
    //val context = InstrumentationRegistry.getInstrumentation().targetContext
    val context = ApplicationProvider.getApplicationContext<Context>()
    val mockContext = object : ContextWrapper(context) {
        override fun getSystemService(name: String): Any? {
            if (name == Context.ALARM_SERVICE) return mockAlarmManager
            return super.getSystemService(name)
        }
    }
    val repeatDays = setOf(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)

    val scheduler = AlarmSchedulerImpl(mockContext)

    @Test
    fun `schedule alarm sets alarm` () {
        scheduler.scheduleAlarm(0, now.hour, now.minute, repeatDays)
        verify {mockAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, any<Long>(), any<PendingIntent>())}
    }
}
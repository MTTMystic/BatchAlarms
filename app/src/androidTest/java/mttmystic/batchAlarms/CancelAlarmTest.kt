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

class CancelAlarmTest {
    val mockAlarmManager = mockk<AlarmManager>(relaxed = true)
    val context = ApplicationProvider.getApplicationContext<Context>()
    val mockContext = object : ContextWrapper(context) {
        override fun getSystemService(name: String): Any? {
            if (name == Context.ALARM_SERVICE) return mockAlarmManager
            return super.getSystemService(name)
        }
    }

    val scheduler = AlarmSchedulerImpl(mockContext)

    @Test
    fun `cancel alarm cancels alarm` () {
        scheduler.cancelAlarm(0)
        verify {mockAlarmManager.cancel( any<PendingIntent>())}
    }
}
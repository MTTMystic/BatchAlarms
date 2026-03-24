package mttmystic.batchAlarms

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import mttmystic.batchAlarms.data.local.AlarmDao
import mttmystic.batchAlarms.data.local.AlarmDatabase
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class AlarmDaoTest {
    private lateinit var alarmDao : AlarmDao
    private lateinit var alarmDb : AlarmDatabase

    @BeforeEach
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        alarmDb = Room.inMemoryDatabaseBuilder(
            context, AlarmDatabase::class.java
        ).build()
        alarmDao = alarmDb.alarmDao()
    }

    @AfterEach
    fun closeDb() {
        alarmDb.close()
    }

}



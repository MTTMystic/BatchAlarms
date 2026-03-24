package mttmystic.batchAlarms

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import batchAlarms.proto.alarm
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import mttmystic.batchAlarms.data.local.Alarm
import mttmystic.batchAlarms.data.local.AlarmDao
import mttmystic.batchAlarms.data.local.AlarmDatabase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class AlarmDaoTest {
    private lateinit var alarmDao : AlarmDao
    private lateinit var alarmDb : AlarmDatabase

    val testAlarm = Alarm(1, 0, 0, "", true)
    val testAlarmList  = listOf(testAlarm, testAlarm.copy(id = 2))
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        alarmDb = Room.inMemoryDatabaseBuilder(
            context, AlarmDatabase::class.java
        ).build()
        alarmDao = alarmDb.alarmDao()
    }

    @After
    fun closeDb() {
        alarmDb.close()
    }

    @Test
    fun `getAll returns empty list when no alarms in database` () = runBlocking {
        assert(alarmDao.getAll().first().isEmpty())
    }

    @Test
    fun `insert alarm adds alarm to database`() = runBlocking {
        alarmDao.insert(testAlarm)
        val allAlarms = alarmDao.getAll().first()
        assert(allAlarms.contains(testAlarm))
    }

    @Test
    fun `insert multiple alarms adds all alarms to database` () = runBlocking {
        alarmDao.insert(*testAlarmList.toTypedArray())
        val allAlarms = alarmDao.getAll().first()
        assert(allAlarms.containsAll(testAlarmList))
    }


    @Test
    fun `delete alarm removes alarm from database`() = runBlocking {
        alarmDao.insert(testAlarm)
        alarmDao.delete(testAlarm)
        assert(alarmDao.getAll().first().isEmpty())
    }

    @Test
    fun `delete alarm by id removes correct alarm from database`() = runBlocking {
        alarmDao.insert(*testAlarmList.toTypedArray())
        alarmDao.deleteById(testAlarm.id)
        assertFalse(alarmDao.getAll().first().contains(testAlarm))
    }

    @Test
    fun `getById returns correct alarm`() = runBlocking {
        alarmDao.insert(*testAlarmList.toTypedArray())
        val result = alarmDao.getById(testAlarm.id)
        assertEquals(testAlarm, result)
    }

    @Test
    fun `getById for multiple alarms returns all alarms`() = runBlocking {
        alarmDao.insert(*testAlarmList.toTypedArray())
        val result = alarmDao.getById(testAlarmList.map {it.id})
        assertEquals(testAlarmList, result)
    }

    @Test
    fun `update alarm updates alarm in database` () = runBlocking {
        alarmDao.insert(testAlarm)
        alarmDao.update(testAlarm.copy(active = !testAlarm.active))
        val result = alarmDao.getById(testAlarm.id)
        assertEquals(testAlarm.copy(active = !testAlarm.active), result )
    }

}
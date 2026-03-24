package mttmystic.batchAlarms.data.repository

import android.app.AlarmManager
import android.content.Context
import android.icu.util.Calendar
import androidx.datastore.core.DataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import batchAlarms.proto.Alarm
import batchAlarms.proto.AlarmList
import com.google.protobuf.LazyStringArrayList.emptyList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import mttmystic.batchAlarms.data.AlarmProto
import mttmystic.batchAlarms.data.local.AlarmDao
import mttmystic.batchAlarms.data.toDomain
import kotlin.collections.forEach
import mttmystic.batchAlarms.data.local.Alarm as AlarmEntity
import mttmystic.batchAlarms.data.models.Alarm as DomainAlarm
//TODO add a function to cancel all alarms without deleting them


interface AlarmRepository {

    fun getAlarmsFlow() : Flow<List<DomainAlarm>>

    suspend fun save(alarm : DomainAlarm)

    suspend fun update(alarmId : Int, updatedAlarm: DomainAlarm)

    suspend fun remove(alarmId: Int)

    suspend fun find(alarmId : Int)
}

class AlarmRepositoryImpl @Inject constructor (
    private val alarmDao : AlarmDao
): AlarmRepository {
    override fun getAlarmsFlow() : Flow<List<DomainAlarm>> {
        return alarmDao.getAll().map {
            alarms ->
            alarms.map{
                it.toDomain()
            }
        }
    }

    override suspend fun save(alarm : DomainAlarm) {

    }

    override suspend fun update(alarmId : Int, updatedAlarm : DomainAlarm) {

    }

    override suspend fun remove(alarmId : Int) {

    }

    override suspend fun find(alarmId : Int) {

    }
}


@Singleton
class oldAlarmRepository @Inject constructor(
    private val alarmListStore: DataStore<AlarmList>,
    @ApplicationContext private val context: Context
) {
    //TODO set the active and millis of each alarm based on
    val alarmsList : StateFlow<List<Alarm>> = alarmListStore.data
        .map { store ->
            store.alarmsList.map {it as Alarm}

        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        ) as StateFlow<List<Alarm>>

    private val _alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //TODO set alarm tag to be [id of last alarm held] + 1 to enable extending span
    private var _alarmTag = 0

    //TODO conversion between 12hr and 24hr format

    private fun _buildAlarm(timeInMillis: Long): Alarm {
        val alarmCalendar: Calendar = Calendar.getInstance().apply {
            setTimeInMillis(timeInMillis)
        }

        //TODO use HOUR for 12hr and HOUR_OF_DAY for 24hr
        val hour = alarmCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = alarmCalendar.get(Calendar.MINUTE)

        val alarm: Alarm = Alarm.newBuilder()
            .setId(_alarmTag)
            .setHour(hour)
            .setMinute(minute)
            .setActive(true)
            .setMillis(timeInMillis)
            .build()

        return alarm
    }

    private fun _buildAlarm(alarmProto : AlarmProto) : Alarm {
        val alarm : Alarm = Alarm.newBuilder()
            .setId(alarmProto.id)
            .setHour(alarmProto.hour)
            .setMinute(alarmProto.minute)
            .setActive(true)
            .setMillis(alarmProto.millis)
            .build()
        return alarm
    }

    suspend fun addAlarm(alarm : Alarm) {
        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .addAlarms(alarm)
                .build()
        }
    }

    /*suspend fun generateAlarmsList(alarmTimes : MutableList<Long> ) {
        //clear previous alarm list
        _alarmTag = 0
        deleteAllAlarms()


        alarmTimes.forEach { it : Long ->
            addAlarm(_buildAlarm(it))
            _alarmTag++
        }
    }*/

    suspend fun generateAlarmsList(alarmList : MutableList<AlarmProto>) {
        deleteAllAlarms()
        alarmList.forEach { it : AlarmProto ->
            addAlarm(_buildAlarm(it))
        }
    }

    suspend fun deleteAllAlarms() {
        //cancelAllAlarms()

        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .clearAlarms()
                .build()
        }
    }

    suspend fun disableAlarm(id: Int) {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    if (it.id == id) {
                        //val alarm = alarmFromProto(it)
                        /*if (it.active) {
                            _cancelAlarm(id, it.hour, it.minute)
                        } else {
                            setAlarm(it)
                        }*/
                        it.toBuilder().setActive(false).build()
                    } else {
                        it
                    }
                }
            currentList.toBuilder()
                .clearAlarms()
                .addAllAlarms(updated)
                .build()
        }
    }
    suspend fun toggleAlarm(id: Int) {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    if (it.id == id) {
                        //val alarm = alarmFromProto(it)
                        /*if (it.active) {
                            _cancelAlarm(id, it.hour, it.minute)
                        } else {
                            setAlarm(it)
                        }*/
                        it.toBuilder().setActive(!it.active).build()
                    } else {
                        it
                    }
                }
            currentList.toBuilder()
                .clearAlarms()
                .addAllAlarms(updated)
                .build()
        }
    }

    suspend fun updateAlarmTime(id: Int, newTimeMillis : Long) {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    if (it.id == id) {
                        it.toBuilder().setMillis(newTimeMillis).build()
                    } else {
                        it
                    }
                }
            currentList.toBuilder()
                .clearAlarms()
                .addAllAlarms(updated)
                .build()
        }
    }

    suspend fun toggleAllAlarms() {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    it.toBuilder().setActive(!it.active).build()
                }
            currentList.toBuilder()
                .clearAlarms()
                .addAllAlarms(updated)
                .build()
        }
    }
}
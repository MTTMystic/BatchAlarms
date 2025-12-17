package mttmystic.batchAlarms.data

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

//TODO add a function to cancel all alarms without deleting them

/*interface AlarmRepository {
    //fun _buildAlarm(timeInMillis: Long) : Alarm
    //suspend fun addAlarm(alarm : Alarm)
    suspend fun generateAlarmsList(span: Span, interval: Interval)
    suspend fun deleteAllAlarms()
    suspend fun toggleAlarm(id: Int)
    suspend fun toggleAllAlarms()
}*/

@Singleton
class AlarmRepository @Inject constructor(
    private val alarmListStore: DataStore<AlarmList>,
    @ApplicationContext private val context: Context
) {
    val alarmsList : StateFlow<List<Alarm>> = alarmListStore.data
        .map {it.alarmsList}
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

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

    suspend fun addAlarm(alarm : Alarm) {
        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .addAlarms(alarm)
                .build()
        }
    }

    suspend fun generateAlarmsList(alarmTimes : MutableList<Long> ) {
        //clear previous alarm list
        _alarmTag = 0
        deleteAllAlarms()


        alarmTimes.forEach { it : Long ->
            addAlarm(_buildAlarm(it))
            _alarmTag++
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
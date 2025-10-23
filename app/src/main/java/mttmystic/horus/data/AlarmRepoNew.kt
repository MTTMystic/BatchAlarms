package mttmystic.horus.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat.getString
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import mttmystic.horus.AlarmHandlerViewModel
import mttmystic.horus.AlarmReceiver
import mttmystic.horus.R
import mttmystic.horus.proto.Alarm as DataStoreAlarm
import mttmystic.horus.proto.AlarmList as DataStoreAlarmList
import mttmystic.horus.proto.Alarm.Time as DataStoreTime
import mttmystic.horus.data.Alarm
import mttmystic.horus.proto.alarmList
import java.io.IOException
import kotlin.collections.get

class AlarmRepositoryNew(
    private val alarmListStore: DataStore<DataStoreAlarmList>,
    private val context: Context
) {
    /*private val _alarms: MutableStateFlow<List<Alarm>> =
        MutableStateFlow<List<Alarm>>(mutableListOf())

    val alarms: StateFlow<List<Alarm>>
        get() =
            _alarms.asStateFlow()
     */


    val _alarmsList: Flow<List<DataStoreAlarm>> = alarmListStore.data.map { it.alarmsList }
    //val alarmsList get() = _alarmsList

    private val _alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private var _alarmTag =
        0 //TODO set alarm tag to be [id of last alarm held] + 1 to enable extending span

    init {
        //TODO get alarms from data store
        Log.d("alarm manager", "initialized repo")
    }

    private fun _msToTime(millisElapsed: Long): Pair<Int, Int> {
        val totalSeconds = millisElapsed / 1000
        val hours = totalSeconds / 3600
        val minutes = ((totalSeconds) % 3600) / 60
        return Pair(hours.toInt(), minutes.toInt())
    }

    private fun _alarmTime(millisElapsed: Long, span: Span): Time {
        val timeSince = _msToTime(millisElapsed)
        return Time(span.start.hour + timeSince.first, span.start.minute + timeSince.second)
    }

    /*fun isAlarmToday(time: Time): Boolean {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        return (time.hour >= currentHour) and (time.minute >= currentMinute)
    }*/

    fun fetchAlarms() {
        //we know that when the alarms list is empty and the alarm tag is 0 no alarms were created yet
        //if alarm tag weren't zero that would mean the user had deleted the alarms they had set
        //since alarm tag gets reset when user creates new alarms
        return
    }

    fun buildProtoAlarm(alarm: Alarm): DataStoreAlarm {
        var time: DataStoreTime = DataStoreTime.newBuilder()
            .setHour(alarm.time.hour)
            .setMinute(alarm.time.minute)
            .build()

        var protoAlarm: DataStoreAlarm = DataStoreAlarm.newBuilder()
            .setId(alarm.id)
            .setTime(time)
            .setActive(alarm.active)
            .setMillis(alarm.timeInMillis)
            .build()

        return protoAlarm
    }

    fun alarmFromProto(alarm : DataStoreAlarm) : Alarm {
       val time = Time(alarm.time.hour, alarm.time.minute)
        return Alarm (
           id = alarm.id,
           time = time,
           timeInMillis = alarm.millis,
           active = alarm.active
       )
    }
    suspend fun addAlarm(newAlarm: DataStoreAlarm) {
        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .addAlarms(newAlarm)
                .build()
        }
    }

    /*suspend fun toggleAlarmStore(targetAlarm: DataStoreAlarm) {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    if (it.id == targetAlarm.id) {
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
    }*/

    suspend fun removeAlarms() {
        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .clearAlarms()
                .build()
        }
    }


    fun setAlarm(timeString: String, timeInMillis: Long, pendingNum: Int) {
        val alarmAction = getString(context, R.string.alarm_action)
        val alarmIDKey = getString(context, R.string.alarm_id_key)
        val alarmTimeKey = getString(context, R.string.alarm_time_key)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, timeString)
            putExtra(
                alarmIDKey,
                pendingNum
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            pendingNum,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    private fun _getAlarm(timeInMillis: Long): Alarm {
        val alarmCalendar: Calendar = Calendar.getInstance().apply {
            setTimeInMillis(timeInMillis)
        }
        val alarmTime = Time(
            hour = alarmCalendar.get(Calendar.HOUR_OF_DAY),
            minute = alarmCalendar.get(Calendar.MINUTE)
        )

        val alarm = Alarm(
            time = alarmTime,
            timeInMillis = timeInMillis,
            id = _alarmTag
        )

        _alarmTag++
        return alarm

    }

    suspend fun generateAlarm(timeInMillis: Long) {
        val alarm = _getAlarm(timeInMillis)
        //_alarms.value = _alarms.value + alarm
        setAlarm(alarm.time.display(), timeInMillis, alarm.id)
        val protoAlarm = buildProtoAlarm(alarm)
        addAlarm(protoAlarm)
    }

    suspend fun setAlarms(span: Span, interval: Interval) {
        _alarmTag = 0
        cancelAllAlarms()

        val calendar: Calendar = Calendar.getInstance().apply {
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, span.start.hour)
            set(Calendar.MINUTE, span.start.minute)
        }

        val step: Long = interval.inMillis()
        val end: Long = calendar.timeInMillis + span.lengthInMillis()
        //val start = calendar.timeInMillis
        var curr = calendar.timeInMillis
        while (curr <= end) {
            if (curr > System.currentTimeMillis()) {
                generateAlarm(curr)
            } else {
                /*val dayInterval = Interval(24*60)
            val newTime = curr + dayInterval.inMillis()
            generateAlarm((newTime))*/
            }

            curr += step
        }

        if ((curr - step) != end) {
            generateAlarm(end)
        }


    }


    suspend fun toggleAlarm(pendingNum: Int) {
        alarmListStore.updateData { currentList ->
            val updated = currentList.alarmsList
                .map {
                    if (it.id == pendingNum) {
                        //val alarm = alarmFromProto(it)
                        val time = Time(it.time.hour, it.time.minute)
                        if (it.active) {
                            _cancelAlarm(pendingNum, time)
                        } else {
                            setAlarm(
                                timeString = time.display(),
                                timeInMillis = it.millis,
                                pendingNum = pendingNum
                            )
                        }
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
        /*val otherAlarms = _alarms.value.filterNot { it.id == pendingNum }
        val alarm = (_alarms.value.filter { it.id == pendingNum })[0]*/
        /*if (alarm.active) {
            _cancelAlarm(pendingNum)
        } else {
            setAlarm(
                timeString = alarm.time.display(),
                timeInMillis = alarm.timeInMillis,
                pendingNum = pendingNum
            )
        }
        val newAlarm = alarm.copy(active = !alarm.active)
        val newAlarms = _alarms.value.filterNot { it.id == pendingNum } + newAlarm
        _alarms.value = newAlarms.sortedBy { it.id }*/
    }

    private fun _cancelAlarm(pendingNum: Int, time : Time) {
        /*val alarm = alarmListStore.data.first().alarmsList.find{it.id == pendingNum}
        var time : Time = Time()
        if (alarm != null) {
            time = Time(alarm.time.hour, alarm.time.minute)
        } else {
            return //TODO handle this error
            Log.d("alarm manager", "no alarm with id ${pendingNum} found")
        }*/


        //val alarm = _alarms.value[pendingNum]
        val alarmAction = getString(context, R.string.alarm_action)
        val alarmIDKey = getString(context, R.string.alarm_id_key)
        val alarmTimeKey = getString(context, R.string.alarm_time_key)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, time.display())
            putExtra(
                alarmIDKey,
                pendingNum
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            pendingNum,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.cancel(pendingIntent)
        Log.d("alarm manager", "cancelled alarm ${pendingNum}")
        //Log.d("alarm manager", "alarm set")
    }

    suspend fun cancelAllAlarms() {
        alarmListStore.data.first().alarmsList.forEach {
            alarm -> _cancelAlarm(alarm.id, Time(alarm.time.hour, alarm.time.minute))
        }
        /*_alarms.value.forEach { alarm ->
            _cancelAlarm(alarm.id)
        }
        _alarms.value = mutableListOf()
        */
        alarmListStore.updateData { currentList ->
            currentList.toBuilder()
                .clearAlarms()
                .build()
        }
    }
}
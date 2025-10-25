package mttmystic.horus.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.core.content.ContextCompat.getString
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import mttmystic.horus.AlarmReceiver
import mttmystic.horus.R
import mttmystic.horus.proto.AlarmList
import mttmystic.horus.proto.Alarm
import kotlin.collections.forEach

//TODO add a function to cancel all alarms without deleting them

class AlarmRepository (
    private val alarmListStore: DataStore<AlarmList>,
    private val context: Context
) {

    val alarmsList : Flow<List<Alarm>> = alarmListStore.data
        .map {it.alarmsList}

    private val _alarmMgr: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //TODO set alarm tag to be [id of last alarm held] + 1 to enable extending span
    private var _alarmTag = 0

    //TODO conversion between 12hr and 24hr format

    private fun timeString(hour: Int, minute: Int) : String {
        return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }

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

    suspend fun generateAlarmsList(span: Span, interval: Interval) {
        //clear previous alarm list
        _alarmTag = 0
        deleteAllAlarms()

        val calendar : Calendar = Calendar.getInstance().apply {
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, span.start.hour)
            set(Calendar.MINUTE, span.start.minute)
        }

        val step: Long = interval.inMillis()
        val end: Long = calendar.timeInMillis + span.lengthInMillis()
        //val start = calendar.timeInMillis
        var curr = calendar.timeInMillis
        while (curr <= end) {
            //TODO possibly eliminate this condition by invalidating input where start is not a time in the future
            if (curr > System.currentTimeMillis()) {
                addAlarm(_buildAlarm(curr))
                _alarmTag++
            }

            curr += step
        }

        if ((curr - step) != end) {
            addAlarm(_buildAlarm(curr))
            _alarmTag++
        }
    }

    private fun setAlarm(alarm: Alarm) {
        val alarmAction = getString(context, R.string.alarm_action)
        val alarmIDKey = getString(context, R.string.alarm_id_key)
        val alarmTimeKey = getString(context, R.string.alarm_time_key)
        val timeString = timeString(alarm.hour, alarm.minute)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, timeString)
            putExtra(
                alarmIDKey,
                alarm.id
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.millis,
            pendingIntent
        )
    }

    private suspend fun setAllAlarms() {
        alarmsList.collect {
            list -> list.forEach {
                alarm -> setAlarm(alarm)

            }
        }
    }

    suspend fun createAlarms(span: Span, interval: Interval) {
        generateAlarmsList(span, interval)
        setAllAlarms()
    }

    private fun _cancelAlarm(id: Int, hour: Int, minute: Int) {
        val alarmAction = getString(context, R.string.alarm_action)
        val alarmIDKey = getString(context, R.string.alarm_id_key)
        val alarmTimeKey = getString(context, R.string.alarm_time_key)
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM"
            putExtra(alarmTimeKey, timeString(hour, minute))
            putExtra(
                alarmIDKey,
                id
            )
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        _alarmMgr.cancel(pendingIntent)
    }

    private suspend fun cancelAllAlarms() {

        val currentList = alarmsList.first()
        currentList.forEach { alarm ->
            _cancelAlarm(alarm.id, alarm.hour, alarm.minute)
        }
        /*alarmsList.collect {
                list -> list.forEach {
                alarm ->
            //_cancelAlarm(alarm.id, alarm.hour, alarm.minute)
                    Log.d("alarm manager", "attempt to cancel alarm")
            }
        }
         */
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

        cancelAllAlarms()
    }
}
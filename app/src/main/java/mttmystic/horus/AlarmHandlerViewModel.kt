package mttmystic.horus

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span
import mttmystic.horus.data.Time


//time should be hh:mm in 24hr format
fun isAlarmToday(time : Time) : Boolean {
    val calendar : Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(System.currentTimeMillis())
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    return (time.hour >= currentHour) and (time.minute >= currentMinute)
}

class AlarmHandlerViewModel (val context:Context) : ViewModel() {


    //private val calendar : Calendar = Calendar.getInstance()

    //private val alarmManager : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    //private var _alarmTag : Int = 0

    //private var _alarms = MutableStateFlow<List<Alarm>>(mutableListOf())
    //val alarms get() = _alarms.asStateFlow()

    /*private fun msToTime(millisElapsed : Long) : Pair<Int, Int> {
        val totalSeconds = millisElapsed / 1000
        val hours = totalSeconds / 3600
        val minutes = ((totalSeconds) % 3600) / 60
        return Pair(hours.toInt(), minutes.toInt())
    }

    private fun alarmTime(millisElapsed: Long) : Time {
        val timeSince = msToTime(millisElapsed)
        return Time(_span.value.start.hour + timeSince.first, _span.value.start.minute + timeSince.second)
    }

    fun setSpan() {
        cancelAlarms()
        //Log.d("alarm view model", "changed span")
        //Log.d("alarm view model", "pending span start time: ${_pendingSpan.startHour} : ${_pendingSpan.startMinute} ")
        _span.value.start = _pendingSpan.start
        _span.value.end = _pendingSpan.end
        //Log.d("alarm view model", "start time: ${_span.value.startHour} : ${_span.value.startMinute} ")
        calendar.apply {
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, _span.value.start.hour)
            set(Calendar.MINUTE, _span.value.start.minute)
        }
    }
    */


    /*
    private fun _setAlarm(timeInMillis: Long, pendingNum:Int) {

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "SET_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            pendingNum,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
        //Log.d("alarm manager", "alarm set")
    }

    fun setSpanAlarms() {
        //the calendar should have been set with the span start hour and start minute
        val millisStep : Long = _interval.value.inMillis()
        //todo add this to start millis from calendar set to span start
        val endMillis : Long = calendar.timeInMillis + _span.value.lengthInMillis()
        val startMillis = calendar.timeInMillis
        var millisCurrent = startMillis
        while (millisCurrent <= endMillis  && ((millisCurrent + millisStep)) <= endMillis) {
            //val minutes_elapsed = (millisCurrent / millisStep) * _interval.value.length
            //Log.d("alarm manager", "alarm step: ${minutes_elapsed} minutes since ${_span.value.start.hour}:${_span.value.start.minute}")
            //TODO set an alarm at millisCurrent
            _setAlarm(
                timeInMillis = millisCurrent,
                pendingNum = _alarmTag
            )

            calendar.setTimeInMillis(millisCurrent)
            val alarm = Alarm(
                time =alarmTime(millisCurrent - startMillis),
                id = _alarmTag,
                timeInMillis = millisCurrent)
            _alarms.value += alarm
            //val alarmTime : Time = alarmTime(millisCurrent - startMillis)
            Log.d("alarm manager", "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}")
            millisCurrent += millisStep
            _alarmTag++
        }

        if (millisCurrent < endMillis) {
            //TODO add another alarm at exactly the end of the span IF such alarm was not set by prior loop
            _setAlarm(timeInMillis = endMillis, pendingNum = _alarmTag)
            val alarm = Alarm(
                time =alarmTime(millisCurrent - startMillis),
                id = _alarmTag,
                timeInMillis = millisCurrent)
            _alarms.value += alarm
            _alarmTag++
            //setAlarmCalendar(_span.value.end.hour, _span.value.end.minute)
            //Log.d("alarm manager", "alarm step: ${_span.value.end.hour}:${_span.value.end.minute}")
        }
    }
     */
    //val alarmPermissionState = rememberPermissionState(Manifest.permission.SCHEDULE_EXACT_ALARM)
    /*fun setAlarmCalendar(hour : Int, minute: Int) {
        calendar.apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        setAlarm(context, calendar.timeInMillis, 0)


    }*/

    /*

    //TODO update the mutable state flow such that it triggers an update for ui
    fun toggleAlarm(pendingNum: Int) {
        val otherAlarms = _alarms.value.filterNot {it.id == pendingNum}
        val alarm = (_alarms.value.filter {it.id == pendingNum})[0]
        if (alarm.active) {
            _cancelAlarm(pendingNum)
        } else {
            _setAlarm(alarm.timeInMillis, pendingNum)
        }
        val newAlarm = alarm.copy(active = !alarm.active)
        val newAlarms = _alarms.value.filterNot {it.id == pendingNum} + newAlarm
        _alarms.value = newAlarms.sortedBy { it.id }
    }

    private fun _cancelAlarm(pendingNum:Int) {
        val alarm = _alarms.value[pendingNum]
        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply{
            action = "SET_ALARM"
            putExtra("alarm_time", alarm.time.display())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            pendingNum,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        alarmManager?.cancel(pendingIntent)
        Log.d("alarm manager", "cancelled alarm ${pendingNum}")
        //Log.d("alarm manager", "alarm set")
    }

    fun cancelAlarms() {
        _alarms.value.forEach { alarm ->
            _cancelAlarm(alarm.id)
            _alarms.value = _alarms.value.filterNot {it == alarm}
        }
    }
     */
}


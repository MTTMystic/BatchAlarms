package mttmystic.horus.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mttmystic.horus.data.AlarmRepository
//import mttmystic.horus.data.AlarmRepositoryNew
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span
import mttmystic.horus.data.Time

class CreateAlarmsViewModel(val repository: AlarmRepository) : ViewModel() {
    private var _span = MutableStateFlow(Span())
    val span get() = _span.asStateFlow()
    private var _pendingSpan = Span()
    private var _pendingInterval = Interval(1)
    private var _interval = MutableStateFlow(Interval(1))
    val interval get() = _interval.asStateFlow()

    fun setPendingStart(time : Time) {
        _pendingSpan.start = time
    }

    fun setPendingEnd(time : Time) {
        _pendingSpan.end = time
    }

    /*
    fun validatePendingStart() : Boolean {
        /*val calendar = Calendar.getInstance().apply{
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, _pendingSpan.start.hour)
            set(Calendar.MINUTE, _pendingSpan.start.minute)
        }
        val currentTime = System.currentTimeMillis()
        return calendar.timeInMillis > currentTime*/
        val currentCalendar = Calendar.getInstance()
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)
        return true

    }

    fun validatePendingEnd() : Boolean {
        val startCalendar = Calendar.getInstance().apply{
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, _pendingSpan.start.hour)
            set(Calendar.MINUTE, _pendingSpan.start.minute)
        }

        val endCalendar =  Calendar.getInstance().apply{
            setTimeInMillis(System.currentTimeMillis())
            set(Calendar.HOUR_OF_DAY, _pendingSpan.end.hour)
            set(Calendar.MINUTE, _pendingSpan.end.minute)
        }

        return endCalendar.timeInMillis >= startCalendar.timeInMillis
    }
     */
    fun setPendingInterval(minutes : Int) {
        _pendingInterval = Interval(minutes)
    }

    fun setSpan() {
        _span.value = _pendingSpan
    }

    fun setInterval() {
        _interval.value = _pendingInterval
    }

    fun submit() {
        setSpan()
        setInterval()
        viewModelScope.launch {
            repository.createAlarms(_span.value, _interval.value)
        }

    }
}
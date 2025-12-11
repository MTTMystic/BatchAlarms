package mttmystic.batchAlarms.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
//import mttmystic.batchAlarms.data.AlarmRepositoryNew
import mttmystic.batchAlarms.data.Interval
import mttmystic.batchAlarms.data.Span
import mttmystic.batchAlarms.data.Time
import mttmystic.batchAlarms.domain.ReplaceAlarmsUseCase
import mttmystic.batchAlarms.domain.ValidateIntervalUseCase
import mttmystic.batchAlarms.domain.ValidateSpanLengthUseCase

@HiltViewModel
class CreateAlarmsViewModel @Inject constructor(
    private val replaceAlarmsUseCase: ReplaceAlarmsUseCase,
    private val validateIntervalUseCase: ValidateIntervalUseCase,
    private val validateSpanLengthUseCase: ValidateSpanLengthUseCase
) : ViewModel() {
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

    fun setPendingInterval(minutes : Int) : Boolean {
        if (validateIntervalUseCase(minutes)) {
            _pendingInterval = Interval(minutes)
            return true
        } else {
            return false
        }

    }

    fun setSpan() {
        _span.value = _pendingSpan
    }

    fun setInterval() {
        _interval.value = _pendingInterval
    }

    fun validateSpanLength() : Boolean{
        return validateSpanLengthUseCase(_pendingSpan, _pendingInterval)
    }

    fun validateInterval() : Boolean {
        return validateIntervalUseCase(_pendingInterval.length)
    }
    fun submit() {
        //TODO VALIDATION HERE
        setSpan()
        setInterval()
        viewModelScope.launch {
           replaceAlarmsUseCase(_span.value, _interval.value)
        }

    }
}
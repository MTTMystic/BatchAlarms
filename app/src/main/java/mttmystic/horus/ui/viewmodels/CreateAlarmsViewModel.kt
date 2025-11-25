package mttmystic.horus.ui.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mttmystic.horus.data.AlarmRepository
//import mttmystic.horus.data.AlarmRepositoryNew
import mttmystic.horus.data.Interval
import mttmystic.horus.data.Span
import mttmystic.horus.data.Time
import mttmystic.horus.domain.CreateAlarmsUseCase
import mttmystic.horus.domain.ValidateIntervalUseCase
import mttmystic.horus.domain.ValidateSpanLengthUseCase

@HiltViewModel
class CreateAlarmsViewModel @Inject constructor(
   private val createAlarmsUseCase: CreateAlarmsUseCase,
    private val validateIntervalUseCase: ValidateIntervalUseCase,
    private val validateSpanLengthUseCase: ValidateSpanLengthUseCase
) : ViewModel() {
    private var _span = MutableStateFlow(Span())
    val span get() = _span.asStateFlow()
    private var _pendingSpan = Span()
    private var _pendingInterval = Interval(5)
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
        }

        return false
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
           createAlarmsUseCase(_span.value, _interval.value)
        }

    }
}
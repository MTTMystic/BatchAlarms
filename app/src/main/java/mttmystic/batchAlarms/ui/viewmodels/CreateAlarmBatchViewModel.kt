package mttmystic.batchAlarms.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Character.isDigit
import java.time.DayOfWeek

class CreateAlarmBatchViewModel @Inject constructor(): ViewModel(){
    private var _start : Pair<Int, Int> = Pair(0, 0)
    private var _end : Pair<Int, Int> = Pair(0, 0)
    private var _freq : MutableStateFlow<Int> = MutableStateFlow(5)
    val freq get() = _freq.asStateFlow()

    private var _repeatDays : MutableStateFlow<MutableSet<DayOfWeek>> = MutableStateFlow(mutableSetOf())
    val repeatDays get() = _repeatDays.asStateFlow()

    fun setStart(newStart: Pair<Int, Int>) {
        _start = newStart
    }

    fun setEnd(newEnd : Pair<Int, Int>) {
        _end = newEnd
    }

    fun setFreq(newFreq : Int) {
        _freq.value = newFreq
    }

    fun validateFreqInput(freqText: String) : Boolean {
        if(freqText.isEmpty()) {
            return true
        } else {
            val valid_input = freqText.all {it.isDigit()}
            val freqInt = freqText.trim().toInt()
            val valid_num = freqInt <= 60 && freqInt >= 5
            return valid_input && valid_num
        }
    }

    fun toggleRepeatDay(day : String) {
        if(_repeatDays.value.contains(DayOfWeek.valueOf(day))) {
            _repeatDays.value.remove(DayOfWeek.valueOf(day))
        } else {
            _repeatDays.value.add(DayOfWeek.valueOf(day))
        }
    }re
}
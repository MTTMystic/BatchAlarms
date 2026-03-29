package mttmystic.batchAlarms.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.domain.usecases.CreateSingleAlarm
import java.time.DayOfWeek

class CreateAlarmViewModel @Inject constructor(
    private val createSingleAlarm : CreateSingleAlarm
): ViewModel() {
    private var _time : Pair<Int, Int> = Pair(0, 0)
    //val time get() = _time
    private var _repeatDays : MutableStateFlow<MutableSet<DayOfWeek>> =
        MutableStateFlow(mutableSetOf())
    val repeatDays get() = _repeatDays.asStateFlow()

    fun setTime(newTime: Pair<Int, Int>) {
        _time = newTime
    }

    fun toggleRepeatDay(day : String) {
        if(_repeatDays.value.contains(DayOfWeek.valueOf(day))) {
            _repeatDays.value.remove(DayOfWeek.valueOf(day))
        } else {
            _repeatDays.value.add(DayOfWeek.valueOf(day))
        }
    }

    fun submit() {
        viewModelScope.launch {
            createSingleAlarm(_time.first, _time.second, _repeatDays.value)
        }
    }
}
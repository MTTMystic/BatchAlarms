package mttmystic.batchAlarms.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.AlarmUI
import mttmystic.batchAlarms.domain.usecases.DeleteAlarms
import mttmystic.batchAlarms.domain.usecases.oldGetAlarms
import mttmystic.batchAlarms.domain.usecases.oldToggleAlarm
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import mttmystic.batchAlarms.data.models.uiAlarm
import mttmystic.batchAlarms.domain.usecases.GetAlarms
import mttmystic.batchAlarms.domain.usecases.ToggleAlarms

//import mttmystic.batchAlarms.data.AlarmRepositoryNew

/*class AlarmListViewModelOLD(val repository: oldAlarmRepository) : ViewModel() {
    fun getAlarms() : StateFlow<List<Alarm>> {
        return repository.alarms
    }

    fun toggleAlarm(pendingNum: Int) {
        repository.toggleAlarm((pendingNum))
    }

    fun cancelAllAlarms() {
        repository.cancelAllAlarms()
    }
}*/

/*class AlarmListViewModel(val repository: oldAlarmRepository) : ViewModel() {
    fun getAlarms() : Flow<List<Alarm>> {
        return repository.alarmsList
    }

    fun toggleAlarm(pendingNum: Int) {
        viewModelScope.launch {
            repository.toggleAlarm((pendingNum))
        }

    }

    fun deleteAllAlarms() {
        viewModelScope.launch {
            repository.deleteAllAlarms()
        }

    }
}*/

@HiltViewModel
class AlarmListViewModel @Inject constructor(
    private val toggleAlarmUseCase: ToggleAlarms,
    private val getAlarmsUseCase : GetAlarms,
    private val deleteAlarms: DeleteAlarms,
) : ViewModel() {

    private var _selectedIds = MutableStateFlow<Set<Int>>(mutableSetOf())
    val selectedIds get() = _selectedIds.asStateFlow()

    val inSelectionMode : StateFlow<Boolean> = _selectedIds.map {it.isNotEmpty()}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val allSelected : StateFlow<Boolean> = _selectedIds.map {it.size == getAlarms().value.size}.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    private var _toggleSelectedActive = MutableStateFlow<Boolean>(false)
    val toggleSelectedActive = _toggleSelectedActive.asStateFlow()

    fun getAlarms() : StateFlow<List<uiAlarm>> {
         //TODO fix this lol
         return getAlarmsUseCase().stateIn(
             scope = viewModelScope,
             started = SharingStarted.Eagerly,
             initialValue = emptyList()
         )
        /*return oldGetAlarms().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )*/
    }

    fun toggleAlarm(id: Int) {
        viewModelScope.launch {
            toggleAlarmUseCase(id)
        }


    }

    fun deleteAlarm(id : Int) {
        viewModelScope.launch {
            deleteAlarms(id)
        }
    }


    fun deleteSelected() {
        viewModelScope.launch {
           deleteAlarms(_selectedIds.value)
        }
    }

    fun onAlarmLongPress(alarmId: Int) {
        _selectedIds.value = _selectedIds.value + alarmId
        Log.d("SELECTIONDEBUG", "long press")
    }

    fun onAlarmClick(alarmId: Int) {
        if (_selectedIds.value.contains(alarmId)) {
            _selectedIds.value = _selectedIds.value - alarmId
        } else {
            _selectedIds.value = _selectedIds.value + alarmId
        }
    }

    fun clearSelected() {
        _selectedIds.value = emptySet()
    }

    fun toggleSelected() {
        viewModelScope.launch {
            toggleAlarmUseCase(_selectedIds.value, toggleSelectedActive.value)
        }
    }


    /*

    fun toggleSelectAll() {
        val alarms = getAlarms().value
        val allSelected = alarms.size  == selectedIds.value.size
        if (allSelected) {
            clearSelected()
        } else {
            alarms.forEach {
                _selectedIds.value.add(it.alarm.id)
            }
        }
    }*/

}
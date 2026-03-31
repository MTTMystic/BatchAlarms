package mttmystic.batchAlarms.ui.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.AlarmUI
import mttmystic.batchAlarms.domain.usecases.DeleteAlarms
import mttmystic.batchAlarms.domain.usecases.oldGetAlarms
import mttmystic.batchAlarms.domain.usecases.oldToggleAlarm
import kotlinx.coroutines.flow.flowOf
import mttmystic.batchAlarms.data.models.uiAlarm
import mttmystic.batchAlarms.domain.usecases.GetAlarms
import mttmystic.batchAlarms.domain.usecases.ToggleAlarm

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
    private val toggleAlarmUseCase: ToggleAlarm,
    private val getAlarmsUseCase : GetAlarms,
    private val deleteAlarms: DeleteAlarms
) : ViewModel() {

    private var _selectedIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedIds get() = _selectedIds.asStateFlow()

    val inSelectionMode = _selectedIds.value.isNotEmpty()
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

    fun deleteAllAlarms() {
        viewModelScope.launch {
           deleteAlarms()
        }

    }
}
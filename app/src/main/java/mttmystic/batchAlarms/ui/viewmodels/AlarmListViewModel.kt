package mttmystic.batchAlarms.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.AlarmUI
import mttmystic.batchAlarms.domain.usecases.DeleteAlarms
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
    private val getAlarms: GetAlarms,
    private val toggleAlarm: ToggleAlarm,
    private val deleteAlarms: DeleteAlarms
) : ViewModel() {


     fun getAlarms() : StateFlow<List<AlarmUI>> {
        return getAlarms().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }

    fun toggleAlarm(id: Int) {
        viewModelScope.launch {
            toggleAlarm(id)
        }

    }

    fun deleteAllAlarms() {
        viewModelScope.launch {
           deleteAlarms()
        }

    }
}
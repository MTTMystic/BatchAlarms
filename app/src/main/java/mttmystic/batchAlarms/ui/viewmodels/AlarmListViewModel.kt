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
import mttmystic.batchAlarms.domain.usecases.DeleteAlarmsUseCase
import mttmystic.batchAlarms.domain.usecases.GetAlarmsUseCase
import mttmystic.batchAlarms.domain.usecases.ToggleAlarmUseCase

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
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase,
    private val deleteAlarmsUseCase: DeleteAlarmsUseCase
) : ViewModel() {


     fun getAlarms() : StateFlow<List<AlarmUI>> {
        return getAlarmsUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    }

    fun toggleAlarm(id: Int) {
        viewModelScope.launch {
            toggleAlarmUseCase(id)
        }

    }

    fun deleteAllAlarms() {
        viewModelScope.launch {
           deleteAlarmsUseCase()
        }

    }
}
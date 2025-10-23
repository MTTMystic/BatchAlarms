package mttmystic.horus.ui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mttmystic.horus.data.Alarm
import mttmystic.horus.proto.Alarm as DataStoreAlarm
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.AlarmRepositoryNew

class AlarmListViewModelOLD(val repository: AlarmRepository) : ViewModel() {
    fun getAlarms() : StateFlow<List<Alarm>> {
        return repository.alarms
    }

    fun toggleAlarm(pendingNum: Int) {
        repository.toggleAlarm((pendingNum))
    }

    fun cancelAllAlarms() {
        repository.cancelAllAlarms()
    }
}

class AlarmListViewModel(val repository: AlarmRepositoryNew) : ViewModel() {
    fun getAlarms() : Flow<List<DataStoreAlarm>> {
        return repository._alarmsList
    }

    fun toggleAlarm(pendingNum: Int) {
        viewModelScope.launch {
            repository.toggleAlarm((pendingNum))
        }

    }

    fun cancelAllAlarms() {
        viewModelScope.launch {
            repository.cancelAllAlarms()
        }

    }
}
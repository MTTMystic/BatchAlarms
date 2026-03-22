package mttmystic.batchAlarms.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import mttmystic.batchAlarms.data.models.Settings
import mttmystic.batchAlarms.data.repository.oldSettingsRepository

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val oldSettingsRepository : oldSettingsRepository
) : ViewModel() {

   fun getSettings() : StateFlow<Settings> {
        return oldSettingsRepository.settings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Settings()
        )
    }

    fun toggle24HrFormat() {
        viewModelScope.launch {
            oldSettingsRepository.toggle24HrFormat()
        }

    }

    fun togglePersistAlarms() {
        viewModelScope.launch {
            oldSettingsRepository.togglePersistAlarms()
        }

    }
}
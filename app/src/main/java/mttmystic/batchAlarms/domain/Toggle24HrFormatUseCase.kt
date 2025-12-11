package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.SettingsRepository

class Toggle24HrFormatUseCase @Inject constructor (
    private val settingsRepository : SettingsRepository
){
    suspend operator fun invoke() {
        settingsRepository.toggle24HrFormat()
    }
}
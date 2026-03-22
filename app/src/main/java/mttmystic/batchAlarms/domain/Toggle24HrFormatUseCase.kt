package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.repository.oldSettingsRepository

class Toggle24HrFormatUseCase @Inject constructor (
    private val oldSettingsRepository : oldSettingsRepository
){
    suspend operator fun invoke() {
        oldSettingsRepository.toggle24HrFormat()
    }
}
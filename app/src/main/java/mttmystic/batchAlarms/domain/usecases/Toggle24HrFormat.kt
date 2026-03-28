package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import mttmystic.batchAlarms.data.repository.oldSettingsRepository

class Toggle24HrFormat @Inject constructor (
    private val oldSettingsRepository : oldSettingsRepository
){
    suspend operator fun invoke() {
        oldSettingsRepository.toggle24HrFormat()
    }
}
package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import mttmystic.batchAlarms.data.repository.oldSettingsRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeStringUseCase @Inject constructor (
    private val oldSettingsRepository: oldSettingsRepository
) {
    suspend operator fun invoke(hour24 : Int, minute: Int) : String {
        val use24hr = oldSettingsRepository.settings.first().use24Hr
        val time = LocalTime.of(hour24, minute)
        val formatter = if ( use24hr) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("h:mm a")
        }

        return time.format(formatter)

    }
}
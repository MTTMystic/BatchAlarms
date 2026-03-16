package mttmystic.batchAlarms.domain

import jakarta.inject.Inject
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import mttmystic.batchAlarms.data.SettingsRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeStringUseCase @Inject constructor (
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(hour24 : Int, minute: Int) : String {
        val use24hr = settingsRepository.settings.first().use24Hr
        val time = LocalTime.of(hour24, minute)
        val formatter = if ( use24hr) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("h:mm a")
        }

        return time.format(formatter)

    }
}
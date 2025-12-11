package mttmystic.batchAlarms.domain

import jakarta.inject.Inject

class ValidateIntervalUseCase @Inject constructor() {
    operator fun invoke(length : Int) : Boolean{
        return (length >= 5 && length <= 60)
    }
}
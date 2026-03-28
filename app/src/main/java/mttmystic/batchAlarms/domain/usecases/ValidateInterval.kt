package mttmystic.batchAlarms.domain.usecases

import jakarta.inject.Inject

class ValidateInterval @Inject constructor() {
    operator fun invoke(length : Int) : Boolean{
        return (length >= 5 && length <= 60)
    }
}
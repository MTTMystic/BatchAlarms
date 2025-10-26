package mttmystic.horus.domain

import jakarta.inject.Inject

class ValidateIntervalUseCase @Inject constructor() {
    operator fun invoke(length : Int) : Boolean{
        return (length >= 1 && length <= 60)
    }
}
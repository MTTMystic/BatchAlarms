package mttmystic.horus.domain

class ValidateIntervalUseCase {
    operator fun invoke(length : Int) : Boolean{
        return (length >= 5 && length <= 60)
    }
}
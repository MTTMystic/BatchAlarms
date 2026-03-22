package mttmystic.batchAlarms.data.models

data class Settings(
    val use24Hr : Boolean = false,
    val persistAlarms : Boolean = true
)
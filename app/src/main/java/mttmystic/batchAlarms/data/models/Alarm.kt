package mttmystic.batchAlarms.data.models

import java.time.DayOfWeek

data class Alarm (
    val id : Int,
    val hour : Int,
    val minute : Int,
    val repeatDays : Set<DayOfWeek>,
    val acttive : Boolean
)
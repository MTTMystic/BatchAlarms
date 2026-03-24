package mttmystic.batchAlarms.data

import mttmystic.batchAlarms.data.local.Converters
import mttmystic.batchAlarms.data.models.Alarm as DomainAlarm
import mttmystic.batchAlarms.data.local.Alarm as AlarmEntity

fun DomainAlarm.toEntity() : AlarmEntity = AlarmEntity (
    id = id,
    hour = hour,
    minute = minute,
    repeatDays = Converters().setToString(repeatDays),
    active = active
)

fun AlarmEntity.toDomain() : DomainAlarm = DomainAlarm (
    id = id,
    hour = hour,
    minute = minute,
    repeatDays = Converters().stringToSet(repeatDays),
    active = active
)
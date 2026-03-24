package mttmystic.batchAlarms

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.time.DayOfWeek

interface AlarmScheduler {

    fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek>)

    fun cancelAlarm(id: Int)
}


class AlarmSchedulerImpl @Inject constructor (
    @ApplicationContext private val  context : Context
) : AlarmScheduler {

    override fun scheduleAlarm(id: Int, hour : Int, minute : Int, repeatDays : Set<DayOfWeek>) {}

    override fun cancelAlarm(id: Int) {}
}
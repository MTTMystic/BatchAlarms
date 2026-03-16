package mttmystic.batchAlarms.data

import batchAlarms.proto.Alarm

class Time(var hour: Int = 0,
                var minute: Int = 0) {
    fun display() : String {
        return  "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }
}


class Span(
    var start : Time = Time(),
    var end : Time = Time()
) {
    //get the length of the interval in minutes
    fun lengthInMinutes() : Int {
        //val hoursDiff = this.endHour - this.startHour
        //start minutes since midnight
        var minutesStart : Int = (this.start.hour * 60) + this.start.minute
        //end minutes since midnight
        var minutesEnd : Int = (this.end.hour * 60) + this.end.minute
        //if minutesEnd < minutesStart
        if (minutesEnd <= minutesStart) {
            minutesEnd = minutesEnd +  1440
        }

        return (minutesEnd - minutesStart)
    }

    fun lengthInMillis() : Long {
        return (this.lengthInMinutes() * 60 * 1000).toLong()
    }
}

class Interval(var length : Int = 5) {
    //this should be in minutes

    fun lengthInMillis() : Long {
        return (this.length * 60 * 1000).toLong()
    }
}

data class AlarmProto(
    val hour: Int,
    val minute : Int,
    val millis : Long,
    val id : Int
)

data class AlarmUI(
    val protoAlarm : Alarm,
    val nextTimeLabel : String = "upcoming",
    val timeString : String
)

data class Settings(
    val use24Hr : Boolean = false,
    val persistAlarms : Boolean = true
)























































































enum class mode {
    MULTI,
    SINGLE
}

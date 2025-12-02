package mttmystic.horus.data

import kotlin.times

data class Time(var hour: Int = 0,
                var minute: Int = 0) {
    fun display() : String {
        return  "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
    }
}

data class Span(
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

data class Interval(var length : Int = 5) {
    //this should be in minutes

    fun inMillis() : Long {
        return (this.length * 60 * 1000).toLong()
    }
}


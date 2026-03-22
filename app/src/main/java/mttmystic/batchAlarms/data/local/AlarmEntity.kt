package mttmystic.batchAlarms.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.DayOfWeek


class Converters {
    @TypeConverter
    fun setToString(set : Set<DayOfWeek>?) : String? {
        //val dayStrings = set?.map {it.toString()}
        return set?.joinToString(","){it.name}
    }

    @TypeConverter
    fun stringToSet(setString : String?) : Set<DayOfWeek>? {
        val dayStrings = setString?.split(",")
        val dayOfWeekSet = dayStrings?.map{DayOfWeek.valueOf(it)}?.toSet()
        return dayOfWeekSet
    }
}

@Entity
@TypeConverters(Converters::class)
data class Alarm(
    @PrimaryKey val id: Int,
    val hour: Int,
    val minute: Int,
    val repeatDays : String,
    val active : Boolean
)
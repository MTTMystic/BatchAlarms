package mttmystic.batchAlarms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    fun insert(vararg alarms : Alarm)

    @Delete
    fun delete(alarm : Alarm)

    @Query("SELECT * FROM alarm")
    fun getAll() : Flow<List<Alarm>>

    @Query("SELECT * from alarm WHERE id = :alarmId")
    fun getById(alarmId: Int) : Alarm

     @Query("SELECT * from alarm WHERE id IN (:alarmIds)")
     fun getById(alarmIds : List<Int>) : List<Alarm>
}
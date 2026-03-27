package mttmystic.batchAlarms.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    fun insert(vararg alarms : Alarm) : Long

    @Delete
    fun delete(alarm : Alarm)

    @Query("DELETE FROM alarm WHERE id=:alarmId")
    fun deleteById(alarmId : Int)

    @Update
    fun update(updatedAlarm : Alarm)

    @Query("UPDATE Alarm SET active = :active WHERE id = :alarmId")
    fun updateActive(alarmId:Int, active:Boolean)

    @Query("SELECT * FROM alarm")
    fun getAll() : Flow<List<Alarm>>

    @Query("SELECT * from alarm WHERE id = :alarmId")
    fun getById(alarmId: Int) : Alarm

     @Query("SELECT * from alarm WHERE id IN (:alarmIds)")
     fun getById(alarmIds : List<Int>) : List<Alarm>
}
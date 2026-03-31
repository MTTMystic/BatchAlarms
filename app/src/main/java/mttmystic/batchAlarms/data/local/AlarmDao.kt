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
    suspend fun insert(alarm : Alarm) : Long

    @Delete
    suspend fun delete(alarm : Alarm)

    @Query("DELETE FROM alarm WHERE id=:alarmId")
    suspend fun deleteById(alarmId : Int)

    @Update
    suspend fun update(updatedAlarm : Alarm)

    @Query("UPDATE Alarm SET active = :active WHERE id = :alarmId")
    suspend fun updateActive(alarmId:Int, active:Boolean)

    @Query("SELECT * FROM alarm")
    fun getAll() : Flow<List<Alarm>>

    @Query("SELECT * from alarm WHERE id = :alarmId")
    suspend fun getById(alarmId: Int) : Alarm

     @Query("SELECT * from alarm WHERE id IN (:alarmIds)")
     suspend fun getById(alarmIds : List<Int>) : List<Alarm>
}
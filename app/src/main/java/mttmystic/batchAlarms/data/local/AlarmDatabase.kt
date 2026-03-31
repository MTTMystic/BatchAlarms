package mttmystic.batchAlarms.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.hilt.android.HiltAndroidApp

@Database(entities = [Alarm::class], version = 1, exportSchema = true)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao() : AlarmDao
}
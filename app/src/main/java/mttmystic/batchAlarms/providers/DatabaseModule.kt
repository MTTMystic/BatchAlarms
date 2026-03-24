package mttmystic.batchAlarms.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import batchAlarms.proto.AlarmList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import mttmystic.batchAlarms.data.AlarmListSerializer
import mttmystic.batchAlarms.data.local.AlarmDatabase

@Module
@InstallIn(SingletonComponent::class)
object AlarmDatabaseModule {
    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context : Context) : AlarmDatabase {
       // val DATA_STORE_FILE_NAME = "alarm.pb"
        return Room.databaseBuilder(
            context,
            AlarmDatabase::class.java, "alarm"
        ).build()
    }
}
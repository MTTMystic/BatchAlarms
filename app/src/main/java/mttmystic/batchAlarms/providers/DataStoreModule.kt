package mttmystic.batchAlarms.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import mttmystic.batchAlarms.data.AlarmListSerializer
import batchAlarms.proto.AlarmList

val Context.settingsDataStore : DataStore<Preferences> by preferencesDataStore(
    name = "settings"
)
@Module
@InstallIn(SingletonComponent::class)
object AlarmDataStoreModule {
    @Provides
    @Singleton
    fun provideAlarmListDataStore(@ApplicationContext context : Context) : DataStore<AlarmList> {
        val DATA_STORE_FILE_NAME = "alarm.pb"
        return DataStoreFactory.create(
            produceFile  = {context.dataStoreFile(DATA_STORE_FILE_NAME)},
            serializer = AlarmListSerializer,
        )
    }
}


@Module
@InstallIn(SingletonComponent::class)
object SettingsDataStoreModule {
    @Provides
    @Singleton
    fun provideAlarmListDataStore(@ApplicationContext context : Context) : DataStore<Preferences> {
        return context.settingsDataStore
    }
}
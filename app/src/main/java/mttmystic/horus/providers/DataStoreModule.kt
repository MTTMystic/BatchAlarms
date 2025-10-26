package mttmystic.horus.providers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import mttmystic.horus.data.AlarmListSerializer
import mttmystic.horus.proto.AlarmList

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
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
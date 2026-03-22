package mttmystic.batchAlarms.providers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mttmystic.batchAlarms.data.repository.AlarmRepository
import mttmystic.batchAlarms.data.repository.AlarmRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmRepositoryModule {
    @Binds
    abstract fun bindAlarmRepository(
        alarmRepositoryImpl : AlarmRepositoryImpl
    ) : AlarmRepository
}
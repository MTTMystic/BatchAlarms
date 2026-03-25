package mttmystic.batchAlarms.providers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mttmystic.batchAlarms.AlarmHandler
import mttmystic.batchAlarms.AlarmHandlerImpl
import mttmystic.batchAlarms.AlarmScheduler
import mttmystic.batchAlarms.AlarmSchedulerImpl
import mttmystic.batchAlarms.NotificationHandler
import mttmystic.batchAlarms.NotificationHandlerImpl
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

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmSchedulerModule {
    @Binds
    abstract fun bindAlarmRepository(
        alarmSchedulerImpl : AlarmSchedulerImpl
    ) : AlarmScheduler
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmHandlerModule {
    @Binds
    abstract fun bindAlarmRepository(
        alarmHandlerImpl : AlarmHandlerImpl
    ) : AlarmHandler
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationHandlerModule {
    @Binds
    abstract fun bindAlarmRepository(
        notificationHandlerImpl : NotificationHandlerImpl
    ) : NotificationHandler
}

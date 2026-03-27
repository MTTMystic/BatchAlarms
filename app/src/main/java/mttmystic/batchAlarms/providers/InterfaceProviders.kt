package mttmystic.batchAlarms.providers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mttmystic.batchAlarms.domain.AlarmHandler
import mttmystic.batchAlarms.domain.AlarmHandlerImpl
import mttmystic.batchAlarms.domain.AlarmScheduler
import mttmystic.batchAlarms.domain.AlarmSchedulerImpl
import mttmystic.batchAlarms.domain.NotificationHandler
import mttmystic.batchAlarms.domain.NotificationHandlerImpl
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

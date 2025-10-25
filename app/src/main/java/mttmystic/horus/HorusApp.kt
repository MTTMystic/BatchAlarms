package mttmystic.horus

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.AlarmListSerializer
import mttmystic.horus.domain.AlarmService
//import mttmystic.horus.data.AlarmRepositoryNew
import mttmystic.horus.proto.Alarm
import mttmystic.horus.proto.AlarmList

class HorusApp : Application() {
    lateinit var alarmRepo : AlarmRepository;

    lateinit var alarmService : AlarmService;

    //lateinit var alarmRepoNew : AlarmRepositoryNew;
    lateinit var notificationMgr : Notifications
    private val ALARM_NAME = "alarm"
    private val DATA_STORE_FILE_NAME = "alarm.pb"
    private val SORT_ORDER_KEY = "sort_order"

    private val Context.alarmStore : DataStore<AlarmList> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = AlarmListSerializer
    )


    override fun onCreate() {
        super.onCreate()
        // Initialize AlarmManager and schedule alarms here
        val appContext : Context = applicationContext
        alarmService = AlarmService(appContext)
        alarmRepo = AlarmRepository(alarmStore, appContext)
        //alarmRepoNew = AlarmRepositoryNew(alarmStore, appContext)
        notificationMgr = Notifications(appContext)

    }
}
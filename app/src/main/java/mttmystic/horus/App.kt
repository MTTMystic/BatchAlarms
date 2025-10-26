package mttmystic.horus

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import mttmystic.horus.data.AlarmRepository
import mttmystic.horus.data.AlarmListSerializer
import mttmystic.horus.domain.AlarmService
//import mttmystic.horus.data.AlarmRepositoryNew
import mttmystic.horus.proto.AlarmList

@HiltAndroidApp
class App : Application()
package mttmystic.horus

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
//import mttmystic.horus.data.AlarmRepositoryNew

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var appInit : AppInit

    override fun onCreate() {
        super.onCreate()
        appInit.init()
    }
}
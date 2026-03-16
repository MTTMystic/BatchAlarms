package mttmystic.batchAlarms.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mttmystic.batchAlarms.providers.settingsDataStore
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private object SettingsKeys {
    val TIME_FORMAT = booleanPreferencesKey("time_format")
    val PERSIST_ALARMS = booleanPreferencesKey("persist_alarms")
}

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context : Context,
    settingsDataStore : DataStore<Preferences>
) {
    val settings: Flow<Settings> = settingsDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            Settings(
                use24Hr = prefs[SettingsKeys.TIME_FORMAT] ?: false,
                persistAlarms = prefs[SettingsKeys.PERSIST_ALARMS] ?: true,
            )
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings()
        )
        //.distinctUntilChanged() //only emit when it changes

    suspend fun toggle24HrFormat() {
        context.settingsDataStore.updateData {
            it.toMutablePreferences().also {prefs ->
                val use24HrFormat : Boolean = prefs[SettingsKeys.TIME_FORMAT] ?: true
                prefs[SettingsKeys.TIME_FORMAT] = !use24HrFormat
            }
        }
    }

    suspend fun togglePersistAlarms() {
        context.settingsDataStore.updateData {
            it.toMutablePreferences().also {prefs ->
                val persistAlarms : Boolean = prefs[SettingsKeys.PERSIST_ALARMS] ?: true
                prefs[SettingsKeys.PERSIST_ALARMS] = !persistAlarms
            }
        }
    }
}
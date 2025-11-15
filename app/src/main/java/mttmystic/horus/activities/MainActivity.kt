package mttmystic.horus.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint
import mttmystic.horus.App
import mttmystic.horus.domain.CreateAlarmsUseCase
import mttmystic.horus.domain.DeleteAlarmsUseCase
import mttmystic.horus.domain.GetAlarmsUseCase
import mttmystic.horus.domain.ToggleAlarmUseCase
import mttmystic.horus.domain.ValidateIntervalUseCase
import mttmystic.horus.domain.ValidateSpanLengthUseCase
import mttmystic.horus.ui.AppNavHost
import mttmystic.horus.ui.elements.AlarmsListScreen
import mttmystic.horus.ui.elements.CreateAlarmsScreen
import mttmystic.horus.ui.theme.HorusTheme
import mttmystic.horus.ui.viewmodels.AlarmListViewModel
import mttmystic.horus.ui.viewmodels.CreateAlarmsViewModel


@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as App
        setContent {
            HorusTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                    /*
                    //TODO more sophisticated DI?
                    /*val alarmListViewModel = AlarmListViewModel(
                        GetAlarmsUseCase(app.alarmRepo),
                        ToggleAlarmUseCase(app.alarmRepo, app.alarmService),
                        DeleteAlarmsUseCase(app.alarmRepo, app.alarmService)
                    )

                    val createAlarmsViewModel = CreateAlarmsViewModel(
                        CreateAlarmsUseCase(app.alarmRepo, app.alarmService),
                        ValidateIntervalUseCase(),
                        ValidateSpanLengthUseCase()
                    )
                     */
                    val alarmListViewModel : AlarmListViewModel by viewModels()
                    val createAlarmsViewModel : CreateAlarmsViewModel by viewModels()

                    val notifyPermissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
                    if (!notifyPermissionState.status.isGranted) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val textToShow = if (notifyPermissionState.status.shouldShowRationale) {
                                "Notifications are needed for alarm to function. Please grant the permission and enable notifications." }
                            else {
                                "Notification permission is required. Please grant the permission"
                            }
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp)
                                    .fillMaxWidth(0.75f),
                                text = textToShow)
                            Button(onClick = {notifyPermissionState.launchPermissionRequest()}) {
                                Text("Request permission")
                            }
                        }
                    } else {
                        var showCreateAlarmScreen by remember{
                            mutableStateOf(false)
                        }
                        if (showCreateAlarmScreen) {
                            CreateAlarmsScreen(
                                viewModel = createAlarmsViewModel,
                                onConfirm = {showCreateAlarmScreen = false}, // TODO make this nav back to display alarms screen
                                onDismiss = {
                                    showCreateAlarmScreen = false // TODO make this nav back to display alarms screen
                                })
                        } else {
                            AlarmsListScreen(alarmListViewModel, {showCreateAlarmScreen = true})
                        }
                    }*/
                }

            }
        }
    }
}

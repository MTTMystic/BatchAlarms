package mttmystic.batchAlarms.ui.elements.listscreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import mttmystic.batchAlarms.AlarmHandlerViewModel
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel

@Composable
fun AlarmsListScreen(
    viewmodel : AlarmListViewModel,
    onClickFAB: () -> Unit,
    onClickSettings: () -> Unit
) {

}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun oldAlarmsListScreen(
    viewModel : AlarmListViewModel,
    onClickFAB : () -> Unit,
    onClickSettings : () -> Unit) {
    //val alarmHandler = oldAlarmHandler(LocalContext.current)
    //var showTimePicker by remember{mutableStateOf(false)}
    val alarmsList by viewModel.getAlarms().collectAsState(initial=emptyList())

    DisplayAlarmsScreen(
        alarmsList,
        onClickFAB = onClickFAB,
        onClickCancel = { viewModel.deleteAllAlarms() },
        onClickToggle = { id -> viewModel.toggleAlarm(id) },
        onClickSettings = onClickSettings
    )


    //Text("Granted: ${notificationPermissionState.status.isGranted}")
    RequestPermissions()




}
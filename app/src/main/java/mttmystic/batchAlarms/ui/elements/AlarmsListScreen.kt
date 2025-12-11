package mttmystic.batchAlarms.ui.elements

import android.Manifest
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
//import mttmystic.batchAlarms.AlarmHandlerViewModel
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlarmsListScreen(
    viewModel : AlarmListViewModel,
    onClickFAB : () -> Unit,
    onClickSettings : () -> Unit) {
    //val alarmHandler = AlarmHandler(LocalContext.current)
    //var showTimePicker by remember{mutableStateOf(false)}
    val alarmsList by viewModel.getAlarms().collectAsState(initial=emptyList())
    DisplayAlarmsScreen(
        alarmsList,
        onClickFAB = onClickFAB,
        onClickCancel = { viewModel.deleteAllAlarms() },
        onClickToggle = { id -> viewModel.toggleAlarm(id) },
        onClickSettings = onClickSettings)

    //handling permissions
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    var showRequest by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        Log.d("PERMDEBUG", "Request launched effect")
        if(!notificationPermissionState.status.isGranted) {
            showRequest = true
        }
    }
    //Text("Granted: ${notificationPermissionState.status.isGranted}")
    if (showRequest) {
        RequestPermissions(
            showRationale = notificationPermissionState.status.shouldShowRationale,
            launchRequest = {
                notificationPermissionState.launchPermissionRequest()
                showRequest = false
            })
    }




}
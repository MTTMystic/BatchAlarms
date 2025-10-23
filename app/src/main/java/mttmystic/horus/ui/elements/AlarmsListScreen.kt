package mttmystic.horus.ui.elements

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import mttmystic.horus.AlarmHandlerViewModel
import mttmystic.horus.data.Time
import mttmystic.horus.ui.viewmodels.AlarmListViewModel


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlarmsListScreen(viewModel : AlarmListViewModel, onClickFAB : () -> Unit) {
    //val alarmHandler = AlarmHandler(LocalContext.current)
    //var showTimePicker by remember{mutableStateOf(false)}

    //handling permissions
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    if(!notificationPermissionState.status.isGranted) {
       RequestPermissions(
           showRationale = notificationPermissionState.status.shouldShowRationale,
           launchRequest = { notificationPermissionState.launchPermissionRequest() })
    }

    val alarmsList by viewModel.getAlarms().collectAsState(initial=emptyList())
    DisplayAlarmsScreen(
            alarmsList,
            onClickFAB = onClickFAB,
            onClickCancel = { viewModel.cancelAllAlarms() },
            onClickToggle = { id -> viewModel.toggleAlarm(id) })
}
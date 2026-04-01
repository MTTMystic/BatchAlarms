package mttmystic.batchAlarms.ui.elements.listscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.NonCancellable.isActive
import mttmystic.batchAlarms.R
//import mttmystic.batchAlarms.AlarmHandlerViewModel
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel

@Composable
fun AlarmsListScreen(
    viewModel : AlarmListViewModel,
    onClickFAB: () -> Unit,
    onClickSettings: () -> Unit
) {
    val inSelectionMode by viewModel.inSelectionMode.collectAsState()
    val alarms by viewModel.getAlarms().collectAsState()
    val selectedIds = viewModel.selectedIds.collectAsState()

    if (inSelectionMode) {
        BackHandler { viewModel.clearSelected()}
    }
    Scaffold(
        topBar = {
            if (inSelectionMode) {
                SelectingTopBar(
                    allSelected = selectedIds.value.size == alarms.size,
                    numSelected = selectedIds.value.size)
            } else {
                NonSelectingTopBar()
            }
        },
        floatingActionButton = {
            if (!inSelectionMode) {
                FloatingActionButton(onClick = onClickFAB) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "create alarms"
                    )
                }
            }

       },
    ) {
        innerPadding ->

        val scrollState = rememberScrollState()


        if (alarms.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
                //verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = alarms,
                    key = {it.alarm.id}
                ) {
                    uiAlarm ->
                        Alarm(
                            timeText = uiAlarm.timeLabel,
                            id = uiAlarm.alarm.id,
                            onClickToggle = {id -> viewModel.toggleAlarm(id)},
                            isActive = uiAlarm.alarm.active,
                            nextTimeLabel = uiAlarm.dayLabel,
                            isSelected = selectedIds.value.contains(uiAlarm.alarm.id),
                            onClick = { viewModel.onAlarmClick(uiAlarm.alarm.id)},
                            onLongPress = { viewModel.onAlarmLongPress(uiAlarm.alarm.id) },
                        )
                }
            }
        }
        else {
            Box (
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text="No alarms",
                    fontSize = 36.sp)
            }
        }
    }
}

/*
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




}*/
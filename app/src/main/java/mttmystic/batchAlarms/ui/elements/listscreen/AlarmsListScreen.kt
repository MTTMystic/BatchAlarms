package mttmystic.batchAlarms.ui.elements.listscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
//import mttmystic.batchAlarms.AlarmHandlerViewModel
import mttmystic.batchAlarms.ui.viewmodels.AlarmListViewModel

@Composable
fun AlarmsListScreen(
    viewModel : AlarmListViewModel,
    onClickFAB: () -> Unit,
    onClickSettings: () -> Unit
) {
    val inSelectionMode by viewModel.inSelectionMode.collectAsState()
    val alarms by viewModel.alarms.collectAsState()
    val selectedIds by viewModel.selectedIds.collectAsState()
    val allSelected by viewModel.allSelected.collectAsState()
    if (inSelectionMode) {
        BackHandler { viewModel.clearSelected()}
    }
    Scaffold(
        topBar = {
            if (inSelectionMode) {
                SelectingTopBar(
                    allSelected = allSelected,
                    numSelected = selectedIds.size,
                    toggleSelectedActive = viewModel.toggleSelectedActive.collectAsState().value,
                    onClickToggle = {viewModel.toggleSelected(it)},
                    onClickAll = {
                        Log.d("TAG", "onClickAll fired, allSelected = ${allSelected}, selectedIds=${selectedIds.size}, alarms=${alarms.size}")
                        if (allSelected) {
                            viewModel.clearSelected()
                        } else {
                            viewModel.selectAll()
                        }
                        Log.d("TAG", "onClickAll finished, allSelected = ${allSelected}, selectedIds=${selectedIds.size}, alarms=${alarms.size}")
                    },
                    onClickDelete = {viewModel.deleteSelected()}
                )
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
                            isSelected = selectedIds.contains(uiAlarm.alarm.id),
                            onClick = { viewModel.onAlarmSelectionClick(uiAlarm.alarm.id)},
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
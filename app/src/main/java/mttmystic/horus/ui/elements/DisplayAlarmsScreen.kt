package mttmystic.horus.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import mttmystic.horus.data.AlarmUI
import mttmystic.horus.data.Time
import mttmystic.horus.proto.Alarm as protoAlarm
import mttmystic.horus.ui.elements.Alarm


//import mttmystic.horus.isAlarmToday


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayAlarmsScreen(
    alarms : List<AlarmUI>,
    onClickFAB : () -> Unit,
    onClickCancel : () -> Unit,
    onClickToggle : (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Batch Alarms") },
                actions = {
                    IconButton(onClick = onClickCancel) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "cancel all alarms"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
        )},
        floatingActionButton = {
            FloatingActionButton(onClick = onClickFAB) {
                Icon(
                Icons.Filled.Add,
                contentDescription = "create a new set of alarms"
                )
            } },

    ) { innerPadding ->
        val scrollState = rememberScrollState()

        if (alarms.isNotEmpty()) {
            Column (
                modifier = Modifier
                    .padding(innerPadding)
                .verticalScroll(scrollState)
            ) {
                alarms.forEach { alarm ->
                    val protoAlarm = alarm.protoAlarm
                    val time = Time(protoAlarm.hour, protoAlarm.minute)
                    Alarm (
                        //isToday = isAlarmToday(time),
                        timeText = time.display(),
                        id = protoAlarm.id,
                        onClickToggle = {onClickToggle(protoAlarm.id)},
                        isActive = protoAlarm.active,
                        nextTimeLabel = alarm.nextTimeLabel
                    )
                    //Text(alarm.time.display(), fontSize=48.sp)
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
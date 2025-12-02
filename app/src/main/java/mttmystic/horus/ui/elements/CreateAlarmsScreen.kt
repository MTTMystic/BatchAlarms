package mttmystic.horus.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import mttmystic.horus.R
import mttmystic.horus.data.Time
import mttmystic.horus.ui.viewmodels.CreateAlarmsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAlarmsScreen(
    viewModel: CreateAlarmsViewModel,
    onConfirm : () -> Unit,
    onDismiss : () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var spanLengthInvalid by remember {mutableStateOf(false)}
    var intervalInvalid by remember {mutableStateOf(false)}
    var onDone = {
        intervalInvalid = !viewModel.validateInterval()
        spanLengthInvalid = !viewModel.validateSpanLength()
        if (!intervalInvalid && !spanLengthInvalid) {
            viewModel.submit()
            onConfirm()
        }
    }
    Scaffold (
        topBar = {
            TopAppBar (
                title = { Text("Create Alarms") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "cancel create alarms"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDone) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "create alarms"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        /*bottomBar = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(Modifier.width(10.dp))
                Button(onClick = onDone) {
                    Text("Ok")
                }
            }
        }*/
    ) {

        Box (
            Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .wrapContentSize(align = Alignment.Center)
            ) {
                val (a, b, c) = FocusRequester.createRefs()
                if (spanLengthInvalid) {
                    Text("Please set the time between alarms to be less than the duration of the alarms.")
                }
                Text("First Alarm")
                TimePicker(
                    onChange = {time : Time -> viewModel.setPendingStart(time)},
                )
                //TODO error indication when span length is invalid
                Text("Last Alarm")
                TimePicker(
                    onChange = {time: Time -> viewModel.setPendingEnd(time)}
                )
                Text("Alarms Every")
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,) {
                    IntervalInput(
                        updatePendingInterval  = {minutes : Int -> viewModel.setPendingInterval(minutes)},

                        )
                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                    Text("minutes")
                }
                /*
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(10.dp))
                    Button(onClick = onDone) {
                        Text("Ok")
                    }
                }*/
            }

        }

    }

}

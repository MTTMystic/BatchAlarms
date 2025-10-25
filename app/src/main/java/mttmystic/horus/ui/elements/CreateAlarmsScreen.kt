package mttmystic.horus.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import mttmystic.horus.data.Time
import mttmystic.horus.ui.viewmodels.CreateAlarmsViewModel

@Composable
fun CreateAlarmsScreen(
    viewModel: CreateAlarmsViewModel,
    onConfirm : () -> Unit,
    onDismiss : () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var spanLengthInvalid by remember {mutableStateOf(false)}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .wrapContentSize(align = Alignment.Center)
            .focusGroup()
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
            verticalAlignment = Alignment.CenterVertically) {
            IntervalInput(
                onSubmit = {minutes : Int -> viewModel.setPendingInterval(minutes)})
            Text("minutes")
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
            Spacer(Modifier.width(10.dp))
            Button(onClick =
                {
                    if (viewModel.validateSpanLength()) {
                        viewModel.submit()
                        onConfirm()
                    }
                    else {
                        spanLengthInvalid = true
                    }
                }) {
                Text("Ok")
            }
        }
    }

}

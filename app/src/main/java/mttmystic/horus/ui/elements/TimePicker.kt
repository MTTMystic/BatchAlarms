package mttmystic.horus.ui.elements

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import mttmystic.horus.data.Time


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    modifier : Modifier = Modifier,
    onChange : (Time) -> Unit,
) {

    var allowFocus by remember {mutableStateOf(false)}
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(200)
        allowFocus = true
    }

    val currentTime = Calendar.getInstance()

    //TODO add parameter to determine whether 24hr or 12hr
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )

    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        //onChangeHour(timePickerState.hour)
        onChange(Time(timePickerState.hour, timePickerState.minute))
    }


    /*Box (
        modifier = Modifier
            //.fillMaxSize()
            //.alpha(0.5F)
            .wrapContentSize(align = Alignment.Center)
            //.background(Color.LightGray)
        ,
        contentAlignment = Alignment.Center
    ) {*/
        Box (
            modifier = Modifier
                //.fillMaxSize()
                .padding(20.dp)
                .alpha(1F)
                .width(IntrinsicSize.Max)
        ){
            TimeInput(
                modifier = Modifier
                    .focusProperties {canFocus = allowFocus},
                state = timePickerState,
            )

        }
    //s}

}
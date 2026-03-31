package mttmystic.batchAlarms.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import mttmystic.batchAlarms.R
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmBatchViewModel

@Composable
fun MultiAlarmsContent(
    viewModel : CreateAlarmBatchViewModel,
) {
    //TODO str resource!!
    Text("First Alarm")
    TimePicker(
        onChange = {pair : Pair<Int, Int> -> viewModel.setStart(pair)}
    )
    Text("Last Alarm")
    TimePicker(
        onChange = {pair : Pair<Int, Int> -> viewModel.setEnd(pair)}
    )
    Text("Alarms Every")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FreqInput(
            modifier = Modifier,
            viewModel.freqValid.collectAsState().value,
            viewModel.freqText.collectAsState().value,
            {viewModel.setFreq(it)}
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
        Text("minutes")
    }
}
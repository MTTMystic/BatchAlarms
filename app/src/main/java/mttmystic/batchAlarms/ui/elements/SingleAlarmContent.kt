package mttmystic.batchAlarms.ui.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmViewModel
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmsViewModel

@Composable
fun SingleAlarmContent(
    viewModel : CreateAlarmViewModel
) {
    TimePicker(
        modifier = Modifier,
        onChange = {pair ->  viewModel.setTime(pair) }
    )
}
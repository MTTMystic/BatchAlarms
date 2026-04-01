package mttmystic.batchAlarms.ui.elements.createscreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mttmystic.batchAlarms.ui.viewmodels.CreateAlarmViewModel

@Composable
fun SingleAlarmContent(
    viewModel : CreateAlarmViewModel
) {
    TimePicker(
        modifier = Modifier,
        onChange = { pair -> viewModel.setTime(pair) }
    )
}
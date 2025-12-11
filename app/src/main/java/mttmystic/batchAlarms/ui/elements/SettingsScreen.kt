package mttmystic.batchAlarms.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mttmystic.batchAlarms.data.Settings
import mttmystic.batchAlarms.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onDismiss: () -> Unit
) {
    val settings by viewModel.getSettings().collectAsState()
    Scaffold (
        topBar = {
            TopAppBar (
                title = {Text("Settings")}, navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        }
    ) {
        innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .padding(innerPadding)
        ) {
            Row() {
                Text("Use 24hr format")
                Switch(settings.use24Hr, onCheckedChange = {viewModel.toggle24HrFormat()})
            }
            Row() {
                Text("Persistent alarms")
                Switch(settings.persistAlarms, onCheckedChange = {viewModel.togglePersistAlarms()})
            }
        }
    }
}
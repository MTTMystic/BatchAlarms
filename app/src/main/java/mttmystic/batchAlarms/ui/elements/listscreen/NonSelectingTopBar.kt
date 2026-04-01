package mttmystic.batchAlarms.ui.elements.listscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NonSelectingTopBar(
    //TODO only display this on selection mode -> select all
    onClickDelete : () -> Unit = {},
    onClickSettings : () -> Unit = {}
) {
    TopAppBar(
        //TODO str resource
        title = { Text("Batch Alarms") },
        actions = {
            IconButton(onClick = onClickDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "cancel all alarms"
                )
            }
            IconButton(onClick = onClickSettings) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "go to settings"
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
}
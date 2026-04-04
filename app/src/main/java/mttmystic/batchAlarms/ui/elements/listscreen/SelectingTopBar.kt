package mttmystic.batchAlarms.ui.elements.listscreen

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectingTopBar(
    allSelected : Boolean = false,
    numSelected : Int = 1,
    toggleSelectedActive : Boolean = false,
    onClickToggle : (Boolean) -> Unit = {},
    onClickDelete : () -> Unit = {},
    onClickAll : () -> Unit = {}
    //onBack : () -> Unit = {}
) {
    TopAppBar(
        title = {Text("${numSelected} selected")},
        //TODO
        actions = {
            TextButton(onClick = onClickAll) {

                if (allSelected) {
                    Text("unselect all")
                } else {
                    Text("select all")

                }
            }
            Switch(
                checked = toggleSelectedActive,
                onCheckedChange = { onClickToggle(it)},
            )
            IconButton(onClick = onClickDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete all alarms"
                )
            }
        }
    )
}
package mttmystic.batchAlarms.ui.elements.listscreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectingTopBar(
    allSelected : Boolean = false,
    numSelected : Int = 1,
    onClickToggle : () -> Unit = {},
    onClickDelete : () -> Unit = {},
    //onBack : () -> Unit = {}
) {
    TopAppBar(
        title = {Text("${numSelected} selected")}
        //TODO
    )
}
package mttmystic.batchAlarms.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

fun validateIntervalLength(length : Int) : Boolean {
    return (length >= 5 && length <= 60)
}
fun validateIntervalInput(text : String) : Boolean{
    return text.isEmpty() || text.all {it.isDigit()}
}

@Composable
fun IntervalInput(
    modifier : Modifier = Modifier,
    updatePendingInterval: (pendingInterval: Int) -> Boolean,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var num by remember { mutableStateOf("")}
    var inputError by remember {mutableStateOf(false)}

    Column {
        if(inputError) {
            Text("Please enter a value between 5 and 60")
        }
        TextField(
            value = num,
            onValueChange = {
                if  (validateIntervalInput(it)) {
                    num = it
                }
            },
            label = @Composable {Text("interval")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = inputError,
            keyboardActions = KeyboardActions(
                onDone = {
                    //TODO all this should be done in ValidateIntervalUseCase
                    // and reuse that use case here + when submitting entire form
                    val success = validateIntervalInput(num) && updatePendingInterval(num.trim().toInt())
                    if (success) {
                        num = String.format("%02d", num.trim().toInt())
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                    } else {
                        inputError = true
                    }
                }
            )
        )
    }




}
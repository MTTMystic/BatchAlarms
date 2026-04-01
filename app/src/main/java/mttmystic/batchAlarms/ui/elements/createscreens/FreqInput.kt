package mttmystic.batchAlarms.ui.elements.createscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun FreqInput(
    modifier : Modifier = Modifier,
    inputValid : Boolean,
    freqText : String,
    onChange : (newFreqText : String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier) {
        //TODO extract this to string resource
        if(!inputValid) {
            Text("Please enter a number between 5 and 60", fontSize = 12.sp)
        }

        TextField(
            value = freqText,
            onValueChange = {onChange(it)},
            modifier = Modifier,
            label = @Composable {Text("frequency")},
            isError = !inputValid,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    //TODO all this should be done in ValidateInterval
                    // and reuse that use case here + when submitting entire form
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
            ),
            singleLine = true
        )

    }
}
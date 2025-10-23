package mttmystic.horus.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun RequestPermissions(showRationale : Boolean, launchRequest : () -> Unit) {
    Column {
        val textToShow = if(showRationale) {
            "Horus needs to send notifications for the alarms. Please grant the permission."
        } else {
            "Notification permission is required for this feature to be available. Please grant the permission"
        }

        Text(textToShow)
        Button(onClick=launchRequest) {
            Text("Request Permission")
        }

    }
}
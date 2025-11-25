package mttmystic.horus.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun RequestPermissions(showRationale : Boolean, launchRequest : () -> Unit) {
    Surface (
        Modifier
            .fillMaxSize()

    ) {
        Box (
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column (
                Modifier.fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textToShow = if(showRationale) {
                    "Horus needs to send notifications for the alarms. Please grant the permission."
                } else {
                    "Notification permission is required for the alarms. Please grant the permission"
                }

                Text(
                    text = textToShow,
                textAlign = TextAlign.Center )
                Button(onClick=launchRequest) {
                    Text("Request Permission")
                }


            }
        }

    }


}
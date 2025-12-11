package mttmystic.batchAlarms.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import mttmystic.batchAlarms.R

@Composable
fun RequestPermissions(showRationale : Boolean, launchRequest : () -> Unit) {
    val rationaleText = "Please grant notification permissions for the alarms."
    val noRationaleText = "Alarm notifications are required. Please grant the permission."
    Surface (
        Modifier
            .fillMaxSize()

    ) {
        Box (
            Modifier
                //.fillMaxSize()
                ,
            contentAlignment = Alignment.Center,
        ){
            Card {
                Column (
                    Modifier
                        .fillMaxWidth(0.9f)
                        //.clip(RoundedCornerShape(16.dp))
                        //.background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(dimensionResource(R.dimen.padding_small)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textToShow = if(showRationale) {
                        rationaleText
                    } else {
                        noRationaleText
                    }

                    Text(
                        text = textToShow,
                        textAlign = TextAlign.Center )
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                    Button(onClick=launchRequest) {
                        Text("Grant Permission")
                    }


                }
            }

        }

    }


}
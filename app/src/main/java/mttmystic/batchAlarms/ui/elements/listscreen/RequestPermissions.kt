package mttmystic.batchAlarms.ui.elements.listscreen

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import mttmystic.batchAlarms.R
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestPermissions() {
    val rationaleText = "Please grant notification permissions for the alarms."
    val noRationaleText = "Alarm notifications are required. Please grant the permission."

    //handling permissions
    val notificationPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    var showRequest by remember {mutableStateOf(false)}

    LaunchedEffect(Unit) {
        //Log.d("PERMDEBUG", "Request launched effect")
        if(!notificationPermissionState.status.isGranted) {
            showRequest = true
        }
    }

    if (showRequest) {
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
                        val textToShow = if(notificationPermissionState.status.shouldShowRationale) {
                            rationaleText
                        } else {
                            noRationaleText
                        }

                        Text(
                            text = textToShow,
                            textAlign = TextAlign.Center )
                        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
                        Button(onClick= {
                            notificationPermissionState.launchPermissionRequest()
                            showRequest = false
                        }) {
                            Text("Grant Permission")
                        }


                    }
                }

            }

        }
    }



}
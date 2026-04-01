package mttmystic.batchAlarms.ui.elements.listscreen

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Alarm(
    timeText : String,
    //isToday : Boolean,
    id : Int,
    onClickToggle : (Int) -> Unit,
    isActive : Boolean,
    nextTimeLabel : String = "upcoming",
    isSelected : Boolean = false,
    onClick : () -> Unit = {},
    onLongPress : () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current

    val description  = if (isActive) {
        nextTimeLabel
    } else {
        "Not Scheduled"
    }

    val toggleToast = Toast.makeText(LocalContext.current, "alarm canceled", Toast.LENGTH_SHORT)
    //change bg color based on whether it's selected or not
    val bgColor by animateColorAsState(
        targetValue = if(isSelected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainer
        },
        label = "background color"
    )

    Box (
        modifier = Modifier
            //.background(Color.LightGray) //TODO replace with theme colors

            .fillMaxWidth()
            //.clip()
            .padding(horizontal = 15.dp)
            .padding(bottom = 5.dp)
            .background(bgColor, RoundedCornerShape(16.dp))
            .padding(10.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongPress()
                }
            )
    ) {
        Column () {
            Text(description)
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text=timeText,
                    fontSize = 48.sp
                )
                Switch(checked = isActive,
                    onCheckedChange = {
                        onClickToggle(id)
                        if (isActive) {toggleToast.show()}

                    })
            }
        }


    }
}
package kz.flyingv.remindme.ui.main.remindtime

import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChangeRemindTime(onDismiss:() -> Unit, viewModel: ChangeTimeViewModel = viewModel()) {
    val context = LocalContext.current

    val timeState = viewModel.changeTimeStateFlow.collectAsState().value

    AlertDialog(
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "SET REMIND TIME",
                style = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Center)
            )
        },
        text = {
            AndroidView(
                {
                    val picker = TimePicker(it)
                    picker.setIs24HourView(DateFormat.is24HourFormat(context))
                    if (Build.VERSION.SDK_INT >= 23 ){picker.hour = timeState.remindTime.hour}else{picker.currentHour = timeState.remindTime.hour}
                    if (Build.VERSION.SDK_INT >= 23 ){picker.minute = timeState.remindTime.minute}else{picker.currentMinute = timeState.remindTime.minute}
                    picker.setOnTimeChangedListener { _, hour, time ->
                        Log.d("time updated", " $hour $time " )
                        viewModel.makeAction(
                            ChangeTimeAction.UpdateTime(hour, time)
                        )
                    }
                    picker
                },
                modifier = Modifier.wrapContentWidth(),
            )
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                TextButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        viewModel.makeAction(ChangeTimeAction.SaveTime)
                        onDismiss()
                    }
                ) {
                    Text("SAVE")
                }
            }
        },
        onDismissRequest = { onDismiss()}
    )

}
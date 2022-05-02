package kz.flyingv.remindme.ui.main.remindtime

import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.flyingv.remindme.R

@Composable
fun ChangeRemindTime(onDismiss:() -> Unit, viewModel: ChangeTimeViewModel = viewModel()) {
    val context = LocalContext.current

    val timeState = viewModel.changeTimeStateFlow.collectAsState().value

    Dialog(
        onDismissRequest = {
            viewModel.makeAction(ChangeTimeAction.SaveTime)
            onDismiss()
        }
    ) {
        AndroidView(
            {
                val picker = TimePicker(it)
                picker.setIs24HourView(DateFormat.is24HourFormat(context))
                picker.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_700 ))
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
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
        )
    }

}
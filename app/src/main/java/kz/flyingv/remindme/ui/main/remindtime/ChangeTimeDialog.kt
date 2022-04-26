package kz.flyingv.remindme.ui.main.remindtime

import android.widget.TimePicker
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChangeRemindTime(onDismiss:() -> Unit, viewModel: ChangeTimeViewModel = viewModel()) {

    AlertDialog(
        title = {},
        text = {
            AndroidView(
                { TimePicker(it) },
                update = {},
                modifier = Modifier.wrapContentWidth(),
            )
        },
        buttons = {
            TextButton(onClick = {}) {
                Text("SAVE")
            }
            TextButton(onClick = {}) {
                Text("CANCEL")
            }
        },
        onDismissRequest = { onDismiss()}
    )

}
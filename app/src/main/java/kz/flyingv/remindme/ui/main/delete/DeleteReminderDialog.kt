package kz.flyingv.remindme.ui.main.delete

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.ui.statemodel.RemindFormatter

@Composable
fun DeleteReminderDialog(
    reminder: Reminder?,
    onDismiss:() -> Unit,
    onAccept:() -> Unit
) {

    AlertDialog(
        title = {
            Text((reminder?.name ?: "").plus(" - ").plus(RemindFormatter.formatRemindType(reminder?.type ?: RemindType.Daily)) , style = MaterialTheme.typography.h6, maxLines = 1)
        },
        text = {
            Text("You sure you want to delete it, right?", style = MaterialTheme.typography.body1)
        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    onClick = { onAccept() }
                ) {
                    Text("Yes, delete it")
                }
                Button(
                    onClick = { onDismiss() }
                ) {
                    Text("No, keep it")
                }
            }
        },
        onDismissRequest = {

        }
    )

}
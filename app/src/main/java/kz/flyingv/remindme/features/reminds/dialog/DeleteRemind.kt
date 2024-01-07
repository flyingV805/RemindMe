package kz.flyingv.remindme.features.reminds.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kz.flyingv.remindme.domain.entity.Reminder
import kz.flyingv.remindme.features.reminds.uidata.RemindFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteRemind(
    reminder: Reminder,
    delete: (reminder: Reminder) -> Unit,
    cancel: () -> Unit
){

    BasicAlertDialog(
        onDismissRequest = { cancel() },
        properties = DialogProperties()
    ) {

        Text(
            (reminder.name).plus(" - ").plus(RemindFormatter.formatRemindType(reminder.type)),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1
        )

        Text("You sure you want to delete it, right?", style = MaterialTheme.typography.bodyMedium)

        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = { delete(reminder) }
            ) {
                Text("Yes, delete it")
            }
            Button(
                onClick = { cancel() }
            ) {
                Text("No, keep it")
            }
        }

    }

}
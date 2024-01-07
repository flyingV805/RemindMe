package kz.flyingv.remindme.features.reminds.dialog

import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteRemind(
    onHide: () -> Unit
){

    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties()
    ) {



    }

}
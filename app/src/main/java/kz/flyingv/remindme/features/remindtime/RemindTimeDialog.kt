package kz.flyingv.remindme.features.remindtime

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindTimeDialog(
    viewModel: RemindTimeViewModel = viewModel(),
    hide: () -> Unit
) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()
    val pickerState = rememberTimePickerState(uiState.selectHour, uiState.selectMinute)

    LaunchedEffect(key1 = uiState.hide){
        if(uiState.hide){
            hide()
            viewModel.reduce(RemindTimeAction.Hidden)
        }
    }

    BasicAlertDialog(
        onDismissRequest = { hide() },
        properties = DialogProperties()
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = pickerState)

                Button(
                    onClick = {
                        viewModel.reduce(RemindTimeAction.UpdateTime(pickerState.hour, pickerState.minute))
                    }
                ) {
                    Text("Set time")
                }
                
                Spacer(modifier = Modifier.height(16.dp))

            }


        }


    }

}
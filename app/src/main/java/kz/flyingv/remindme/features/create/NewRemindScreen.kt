package kz.flyingv.remindme.features.create

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun NewRemindScreen(
    viewModel: NewRemindViewModel = viewModel(),
    onHide: () -> Unit
) {



    Button(onClick = {
        onHide()
    }) {
        Text("Hide bottom sheet")
    }

}
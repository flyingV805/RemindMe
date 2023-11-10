package kz.flyingv.remindme.features.reminds

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RemindsScreen(viewModel: RemindsViewModel = viewModel()) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()

}
package kz.flyingv.remindme.features.reminds

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kz.flyingv.remindme.R
import kz.flyingv.remindme.ui.widget.ReminderAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindsScreen(viewModel: RemindsViewModel = viewModel()) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ReminderAppBar(
                onSearchStarted = {},
                onSearchUpdate = {value -> },
                onSearchClosed = {}
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(painterResource(id = R.drawable.ic_baseline_alarm_24), contentDescription = "Localized description")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {  }
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { innerPadding ->



        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            items(50) { item ->
                Text(modifier = Modifier.padding(8.dp), text = "Item $item")
            }
        }

    }

}

@Preview
@Composable
fun RemindsScreenPreview(){
    RemindsScreen()
}
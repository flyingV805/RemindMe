package kz.flyingv.remindme.features.reminds

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.features.create.NewRemindScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindsScreen(viewModel: RemindsViewModel = viewModel()) {

    val uiState by viewModel.provideState().collectAsStateWithLifecycle()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)),
                title = {
                    Crossfade(
                        targetState = uiState.searching,
                        label = "searching"
                    ) { showSearch ->
                        when(showSearch){
                            true -> Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Spacer(modifier = Modifier.width(4.dp))
                                TextField(
                                    value = "",
                                    onValueChange = {},
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("Searching for...") },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                    )
                                )
                                /*TextField(
                                    modifier = Modifier
                                        .weight(1f),
                                    //.focusRequester(focusRequester),
                                    value = "",
                                    placeholder = { Text("Searching for...") },
                                    singleLine = true,
                                    onValueChange = { newValue: String -> },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        focusedIndicatorColor =  Color.Transparent, //hide the indicator
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )*/
                                Box(modifier = Modifier.padding(8.dp)){
                                   IconButton(
                                       onClick = {viewModel.reduce(RemindsAction.EndSearch)}
                                   ) {
                                        Icon(Icons.Filled.Close, "search")
                                    }
                                }
                            }
                            false -> Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Remind Me", style = MaterialTheme.typography.h6)
                                Spacer(Modifier.weight(1f))
                                IconButton(
                                    onClick = { viewModel.reduce(RemindsAction.StartSearch)}
                                ) {
                                    Icon(Icons.Filled.Search, "search")
                                }
                            }
                        }
                    }
                },
                scrollBehavior = scrollBehavior
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
                        onClick = { showBottomSheet = true }
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(Color.Red),
        ) {
            items(50) { item ->
                Text(modifier = Modifier.padding(8.dp), text = "Item $item")
            }
        }

        //add new reminder
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {

                NewRemindScreen(
                    onHide = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }
                )

            }
        }

    }

}

@Preview
@Composable
fun RemindsScreenPreview(){
    RemindsScreen()
}
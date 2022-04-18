package kz.flyingv.remindme.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.flyingv.remindme.activity.main.action.MainAction
import kz.flyingv.remindme.activity.main.fragment.NewReminderDialog
import kz.flyingv.remindme.activity.main.fragment.NewReminderViewModel
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.isInPreview
import kz.flyingv.remindme.ui.previewState
import kz.flyingv.remindme.ui.topbar.CustomTopBar
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val newReminderViewModel: NewReminderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MaterialTheme {
                MainScreen()
            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MainScreen(){
        val scope = rememberCoroutineScope()

        val modalBottomState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {it != ModalBottomSheetValue.HalfExpanded}
        )
        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetShape = shapes.large.copy(topStart = CornerSize(16.dp), topEnd = CornerSize(16.dp)),
            sheetContent = { NewReminderDialog(modalBottomState, newReminderViewModel) }
        ) {
            BackHandler(enabled = modalBottomState.isVisible) {
                scope.launch {modalBottomState.hide()}
            }
            CreateScaffold{
               scope.launch {
                   modalBottomState.animateTo(ModalBottomSheetValue.Expanded)
               }
            }
        }
    }


    private val topBarHeight = 72.dp

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Unit){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700 = Color(0xFF1976D2)

        val mainState = if(!isInPreview()){
            mainViewModel.mainStateFlow.collectAsState().value
        }else{
            previewState()
        }

        val toolbarHeightPx = with(LocalDensity.current) { topBarHeight.roundToPx().toFloat() }
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                    return Offset.Zero
                }
            }
        }

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
            },
            drawerGesturesEnabled = false,
            content = {
                Box(modifier = Modifier.nestedScroll(nestedScrollConnection)) {
                    ReminderList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 8.dp, end = 8.dp),
                        reminders = mainState.reminders,
                    )
                    CustomTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(topBarHeight)
                            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
                        onSearchStarted = {mainViewModel.makeAction(MainAction.StartSearch)},
                        onSearchUpdate = {text -> mainViewModel.makeAction(MainAction.UpdateSearch(text))},
                        onSearchClose = {mainViewModel.makeAction(MainAction.EndSearch)},
                        isSearching = mainState.isSearching,
                        searchValue = mainState.searchText
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showNewReminderDialog()
                    }
                ){
                    Icon(Icons.Filled.Add, "")
                }
            },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    backgroundColor = materialBlue700,
                    cutoutShape = shapes.small.copy(CornerSize(percent = 50)),
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.AccountCircle, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.AccountBox, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Settings, "")
                        }
                    }
                }
            }
        )

    }


    @Composable
    private fun ReminderList(modifier: Modifier, reminders: List<Reminder>){
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(top = topBarHeight, start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            items(
                count = reminders.size,
                itemContent = {
                    ReminderListItem(reminder = reminders[it])
                }
            )
            item{
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }

    @Composable
    fun ReminderListItem(reminder: Reminder) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { },
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
                        .background(Color.Blue, CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(Icons.Filled.ThumbUp, "",
                        Modifier
                            .padding(12.dp)
                            .width(24.dp)
                            .height(24.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(text = reminder.name, style = typography.h6)
                    Text(text = "Every month, 6-th", style = typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Last remind: 2 days ago", style = typography.caption)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Action: Open app - Kaspi.kz", style = typography.caption)
                }
            }
        }
    }
    @Preview
    @Composable
    fun ComposablePreview() {
        MaterialTheme {
            MainScreen()
        }
    }
}
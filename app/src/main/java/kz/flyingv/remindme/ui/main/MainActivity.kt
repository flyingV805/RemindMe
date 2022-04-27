package kz.flyingv.remindme.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.data.model.RemindAction
import kz.flyingv.remindme.data.model.RemindType
import kz.flyingv.remindme.ui.main.create.NewReminderDialog
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.ui.main.remindtime.ChangeRemindTime
import kz.flyingv.remindme.ui.widgets.iconselector.getIcon
import kz.flyingv.remindme.ui.widgets.isInPreview
import kz.flyingv.remindme.ui.widgets.previewMainState
import kz.flyingv.remindme.ui.widgets.topBarHeight
import kz.flyingv.remindme.ui.widgets.topbar.CustomTopBar
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

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
            sheetContent = { NewReminderDialog(modalBottomState) }
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

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Unit){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700 = Color(0xFF1976D2)

        val mainState = if(!isInPreview()){
            mainViewModel.mainStateFlow.collectAsState().value
        }else{
            previewMainState()
        }

        val toolbarHeightPx = with(LocalDensity.current) { topBarHeight.roundToPx().toFloat() }
        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

        val remindersListScrollState = remember{ LazyListState() }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                    val contentScrollOffset = remindersListScrollState.firstVisibleItemScrollOffset
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta

                    if(newOffset.absoluteValue <= contentScrollOffset){
                        toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                    }

                    return Offset.Zero
                }
            }
        }

        val showSettingsDialog = remember{ mutableStateOf(false) }

        Scaffold(
            scaffoldState = scaffoldState,
            drawerGesturesEnabled = false,
            content = {
                Box {
                    ReminderList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .nestedScroll(nestedScrollConnection)
                            .padding(start = 8.dp, end = 8.dp),
                        listState = remindersListScrollState,
                        reminders = mainState.reminders,
                        isInSearch = mainState.isSearching
                    )
                    CustomTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(topBarHeight)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = toolbarOffsetHeightPx.value.roundToInt()
                                )
                            },
                        onSearchStarted = {mainViewModel.makeAction(MainAction.StartSearch)},
                        onSearchUpdate = {text -> mainViewModel.makeAction(MainAction.UpdateSearch(text))},
                        onSearchClose = {mainViewModel.makeAction(MainAction.EndSearch)},
                        isSearching = mainState.isSearching,
                        searchValue = mainState.searchText
                    )
                    if(showSettingsDialog.value) {
                        ChangeRemindTime(
                            onDismiss = {
                                showSettingsDialog.value = false
                            }
                        )
                    }
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
                            Icon(Icons.Filled.Settings, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.AccountBox, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = {
                            showSettingsDialog.value = true
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_baseline_alarm_24),
                                ""
                            )
                        }
                    }
                }
            }
        )

    }

    @Composable
    private fun ReminderList(modifier: Modifier, listState: LazyListState, reminders: List<Reminder>, isInSearch: Boolean){
        //val state = remember{ LazyListState() }
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(top = topBarHeight, start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            //show something, if user don't have any reminders
            if(reminders.isEmpty()){
                item{
                    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie-no-data.json"))
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Spacer(modifier = Modifier.height(36.dp))
                        Text(
                            text = if(!isInSearch){
                                "Oops, you don't have any reminders yet..."
                            }else{
                                "Oops, can't find anything..."
                            },
                            style = typography.h6
                        )
                        LottieAnimation(
                            composition
                        )
                        Text(
                            text = if(!isInSearch){"Create one"}else{""},
                            style = typography.h6
                        )
                    }
                }
            }

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
                    Icon(
                        getIcon(icon = reminder.icon), 
                        "",
                        Modifier
                            .padding(12.dp)
                            .width(28.dp)
                            .height(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(text = reminder.name, style = typography.h6)

                    when(reminder.type){
                        is RemindType.Daily -> {
                            Text(text = "Every day", style = typography.h6)
                        }
                        is RemindType.Monthly -> {
                            Text(text = "Every month, ${reminder.type.dayOfMonth}", style = typography.h6)
                        }
                        is RemindType.Weekly -> {
                            Text(text = "Every week, ${reminder.type.dayOfWeek}", style = typography.h6)
                        }
                        is RemindType.Yearly -> {
                            Text(text = "Every year, ${reminder.type.dayOfMonth}", style = typography.h6)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Last remind: 2 days ago", style = typography.caption)
                    Spacer(modifier = Modifier.height(4.dp))
                    when(reminder.action){
                        RemindAction.DoNothing -> Text(text = "Action: Not assigned", style = typography.caption)
                        is RemindAction.OpenApp -> Text(text = "Action: Open App - ${reminder.action.installedApp?.name}", style = typography.caption)
                        is RemindAction.OpenUrl -> Text(text = "Action: Open URL - ${reminder.action.url}", style = typography.caption)
                        null -> Text(text = "Action: Error", style = typography.caption)
                    }

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
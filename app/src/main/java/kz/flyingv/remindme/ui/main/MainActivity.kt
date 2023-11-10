package kz.flyingv.remindme.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import kz.flyingv.remindme.R
import kz.flyingv.remindme.ui.main.create.NewReminderDialog
import kz.flyingv.remindme.data.model.Reminder
import kz.flyingv.remindme.ui.darkUI
import kz.flyingv.remindme.ui.lightUI
import kz.flyingv.remindme.ui.main.delete.DeleteReminderDialog
import kz.flyingv.remindme.ui.main.remindtime.ChangeRemindTime
import kz.flyingv.remindme.ui.statemodel.RemindFormatter
import kz.flyingv.remindme.ui.widgets.selector.getIcon
import kz.flyingv.remindme.ui.widgets.isInPreview
import kz.flyingv.remindme.ui.widgets.previewMainState
import kz.flyingv.remindme.ui.widgets.topBarHeight
import kz.flyingv.remindme.ui.widgets.topbar.CustomTopBar
import kz.flyingv.remindme.utils.datetime.DatetimeUtils
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MaterialTheme(
                colors = if(isSystemInDarkTheme()) {
                    darkUI()
                }else{
                    lightUI()
                }
            ) {
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
            sheetContent = {
                NewReminderDialog{
                    scope.launch { modalBottomState.hide() }
                    println()
                }
            }
        ) {
            BackHandler(enabled = modalBottomState.isVisible) {
                scope.launch {modalBottomState.hide()}
            }
            CreateScaffold{
               scope.launch {
                   //modalBottomState.animateTo(ModalBottomSheetValue.Expanded)
               }
            }
        }
    }

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Unit){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

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

        val showRemoveReminderDialog = remember{ mutableStateOf(false) }
        val reminderForDelete = remember{ mutableStateOf<Reminder?>(null) }

        val mainColorTop = colorResource(id = R.color.purple_700)

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
                        isInSearch = mainState.isSearching,
                        isInitial = mainState.isInitial,
                        deleteReminder = {reminder ->
                            reminderForDelete.value = reminder
                            showRemoveReminderDialog.value = true
                            println()
                        }
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
                        color = mainColorTop,
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
                    if(showRemoveReminderDialog.value) {
                        DeleteReminderDialog(
                            reminder = reminderForDelete.value,
                            onDismiss = {
                                showRemoveReminderDialog.value = false
                            },
                            onAccept = {
                                mainViewModel.makeAction(MainAction.DeleteReminder(reminderForDelete.value!!))
                                showRemoveReminderDialog.value = false
                            }
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colorResource(id = R.color.purple_700),
                    contentColor = colorResource(id = R.color.white),
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
                    backgroundColor = colorResource(id = R.color.teal_200),
                    cutoutShape = shapes.small.copy(CornerSize(percent = 50)),
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        /*IconButton(onClick = {}) {
                            Icon(Icons.Filled.Settings, "")
                        }
                        Spacer(modifier = Modifier.width(4.dp))*/
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
    private fun ReminderList(modifier: Modifier, listState: LazyListState, reminders: List<Reminder>, isInSearch: Boolean, isInitial: Boolean, deleteReminder: (reminder: Reminder) -> Unit?){
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(top = topBarHeight, start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            //show something, if user don't have any reminders
            if(reminders.isEmpty() && !isInitial){
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
                    ReminderListItem(
                        reminder = reminders[it],
                        deleteReminder = {reminder -> deleteReminder(reminder)}
                    )
                }
            )

            item{
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }

    @Composable
    fun ReminderListItem(reminder: Reminder, deleteReminder: (reminder: Reminder) -> Unit?) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable {
                    //mainViewModel.debugNotification(this, reminder)
                },
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
                        .background(colorResource(id = R.color.purple_200), CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = getIcon(icon = reminder.icon),
                        contentDescription = "",
                        modifier = Modifier.padding(12.dp).width(28.dp).height(28.dp),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(text = reminder.name, style = typography.h6, maxLines = 1)
                    Text(text = RemindFormatter.formatRemindType(reminder.type), style = typography.h6, maxLines = 1)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Last remind: ${DatetimeUtils.lastTimeDisplayed(reminder.lastShow)}", style = typography.caption, maxLines = 1)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = RemindFormatter.formatRemindAction(reminder.action), style = typography.caption, maxLines = 1)
                }
                IconButton(
                    onClick = {
                        deleteReminder(reminder)
                    }
                ) {
                    Icon(Icons.Filled.Delete, "delete")
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
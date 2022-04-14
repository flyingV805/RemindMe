package kz.flyingv.remindme.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.flyingv.remindme.activity.main.action.MainAction
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.selector.SegmentText
import kz.flyingv.remindme.ui.selector.SegmentedControl

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
        val modalBottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetShape = shapes.large.copy(topStart = CornerSize(16.dp), topEnd = CornerSize(16.dp)),
            sheetContent = { NewReminderDialog(modalBottomState) }
        ) {
            CreateScaffold{
                scope.launch { modalBottomState.show() }
            }
        }
    }

    @Composable
    private fun CreateScaffold(showNewReminderDialog: () -> Job){
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700 = Color(0xFF1976D2)

        val mainState = viewModel.mainStateFlow.collectAsState().value

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { CreateTopBar(mainState.isSearching, mainState.searchText) },
            drawerGesturesEnabled = false,
            content = { ReminderList(reminders = mainState.reminders) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {showNewReminderDialog()}
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
    private fun CreateTopBar(isSearching: Boolean, searchValue: String){
        val focusRequester = remember { FocusRequester() }
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.Transparent)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        ){
            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
            ){
                AnimatedVisibility(visible = !isSearching, enter = fadeIn(), exit = fadeOut()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Remind ME", style = typography.h6)
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = {
                            //focusRequester.requestFocus()
                            viewModel.makeAction(MainAction.StartSearch)
                        }) {
                            Icon(Icons.Filled.Search, "search")
                        }
                    }
                }
                AnimatedVisibility(visible = isSearching, enter = fadeIn(), exit = fadeOut()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Spacer(modifier = Modifier.width(4.dp))
                        TextField(
                            modifier = Modifier.weight(1f).focusRequester(focusRequester),
                            value = searchValue,
                            placeholder = {Text("Searching for...")},
                            singleLine = true,
                            onValueChange = { newValue -> viewModel.makeAction(MainAction.UpdateSearch(newValue)) },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor =  Color.Transparent, //hide the indicator
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        Box(modifier = Modifier.padding(8.dp)){
                            IconButton(onClick = {
                                viewModel.makeAction(MainAction.EndSearch)
                            }) {
                                Icon(Icons.Filled.Close, "search")
                            }
                        }
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun NewReminderDialog(dialogState: ModalBottomSheetState){

        val scope = rememberCoroutineScope()

        val threeSegments = remember { listOf("Daily", "Weekly", "Monthly", "Yearly") }
        var selectedThreeSegment by remember { mutableStateOf(threeSegments.first()) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text ="NEW REMINDER", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            SegmentedControl(
                threeSegments,
                selectedThreeSegment,
                onSegmentSelected = { selectedThreeSegment = it }
            ) {
                SegmentText(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text ="VIEW DETAIL", style = typography.h6)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "VIEW DETAIL", style = typography.caption)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Spacer(modifier = Modifier.height(16.dp))
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Create,"") },
                text = { Text("CREATE REMINDER") },
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    scope.launch { dialogState.hide() }
                    viewModel.createReminder()
                }
            )
        }
    }

    @Composable
    private fun ReminderList(reminders: List<Reminder>){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 8.dp, end = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                count = reminders.size,
                itemContent = {
                    ReminderListItem(reminder = reminders[it])
                }
            )
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
            Row(modifier = Modifier.padding(8.dp)) {
                Column {
                    Text(text = "fdgdfg", style = typography.h6)
                    Text(text = reminder.name, style = typography.h6)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "VIEW DETAIL", style = typography.caption)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "VIEW DETAIL", style = typography.caption)
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
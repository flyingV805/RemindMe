package kz.flyingv.remindme.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
        val scope = rememberCoroutineScope()

        val state = viewModel.currentReminders.collectAsState().value
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { CreateTopBar()/*TopAppBar(title = {Text("TopAppBar")}, backgroundColor = materialBlue700)  */},
            //drawerContent = { Text(text = "drawerContent") },
            drawerGesturesEnabled = false,
            content = {
                ReminderList(reminders = state)
            },
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
    private fun CreateTopBar(){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Spacer(modifier = Modifier.width(4.dp))
                Text("Remind ME", style = typography.h6)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.Search, "search")
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
                    Text(text = "VIEW Name", style = typography.h6)
                    Text(text = "VIEW Name", style = typography.h6)
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
        MainScreen()
    }

}
package kz.flyingv.remindme.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.flyingv.remindme.model.Reminder
import kz.flyingv.remindme.ui.selector.MultiSelector
import kz.flyingv.remindme.ui.selector.rememberMultiSelectorState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{MainScreen()}
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun MainScreen(viewModel: MainViewModel = viewModel()){
        val modalBottomState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetShape = shapes.large.copy(topStart = CornerSize(16.dp), topEnd = CornerSize(16.dp)),
            sheetContent = { NewReminderDialog() }
        ) {
            CreateScaffold(viewModel){
                scope.launch { modalBottomState.show() }
            }
        }
    }

    @Composable
    private fun CreateScaffold(viewModel: MainViewModel, addNew: () -> Job){

        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
        val materialBlue700= Color(0xFF1976D2)
        val scope = rememberCoroutineScope()

        val state = viewModel.currentReminders.collectAsState().value
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBar(title = {Text("TopAppBar")}, backgroundColor = materialBlue700)  },
            //drawerContent = { Text(text = "drawerContent") },
            drawerGesturesEnabled = false,
            content = { ReminderList(reminders = state) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                addNew()
                //drawerState.open()
                //viewModel.createReminder()
                Toast.makeText(this, "reminder set", Toast.LENGTH_LONG).show()
            }){
                Text("X")
            } },
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    backgroundColor = materialBlue700,
                    cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
                ) { Text("BottomAppBar") }
            }
        )
    }

    @Composable
    private fun NewReminderDialog(){

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MultiSelector(
                modifier = Modifier.height(48.dp),
                options = listOf("1231", "1233", "1232", "1234"),
                selectedOption = "1231",
                onOptionSelect = {}
            )
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
            Text(text ="VIEW DETAIL", style = typography.h6)
            Text(text = "VIEW DETAIL", style = typography.caption)
        }
    }

    @Composable
    private fun ReminderList(reminders: List<Reminder>){
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
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
        Row {
            Column {
                Text(text = reminder.name, style = typography.h6)
                Text(text = "VIEW DETAIL", style = typography.caption)
            }
        }
    }

    @Preview
    @Composable
    fun ComposablePreview() {
        MainScreen()
    }

}
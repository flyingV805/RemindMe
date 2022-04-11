package kz.flyingv.remindme.activity.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import kz.flyingv.remindme.notifications.Notificator

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{MainScreen()}
    }

    @Composable
    private fun MainScreen(viewModel: MainViewModel = viewModel()){
        val materialBlue700= Color(0xFF1976D2)
        val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = { TopAppBar(title = {Text("TopAppBar")}, backgroundColor = materialBlue700)  },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = { FloatingActionButton(onClick = {
                //remindScheduler.startIfNotSet(10)
                Notificator(applicationContext).makeTestNotification()
                Toast.makeText(this, "reminder set", Toast.LENGTH_LONG).show()
            }){
                Text("X")
            } },
            drawerContent = { Text(text = "drawerContent") },
            content = { Text("BodyContent") },
            bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("BottomAppBar") } }
        )
    }

    @Preview
    @Composable
    fun ComposablePreview() {
        MainScreen()
    }

}
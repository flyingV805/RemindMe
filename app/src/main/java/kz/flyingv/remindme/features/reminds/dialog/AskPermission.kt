package kz.flyingv.remindme.features.reminds.dialog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

private fun getPermissionsList(): List<String>{
    val permissions = ArrayList<String>()

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
        permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
    }

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        permissions.add(Manifest.permission.USE_EXACT_ALARM)
    }

    return permissions
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AskPermissionDialog(
    askNotifications: Boolean,
    askAlarms: Boolean,
    hide: () -> Unit
) {

    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = getPermissionsList(),
        onPermissionsResult = {
            if( askAlarms && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ) {
                context.startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                hide()
            }
        }
    )

    BasicAlertDialog(
        onDismissRequest = { hide() },
        properties = DialogProperties()
    ) {

        Card {

            Column (
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.End
            ){

                Text(
                    "This app uses notifications and alarms for reminders",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "For this to work correctly, you must obtain permission to display notifications and create alarms on your phone.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        //hide()
                        permissionState.launchMultiplePermissionRequest()
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch( Manifest.permission.POST_NOTIFICATIONS)
                        }*/
                    }
                ) {
                    Text("OK, got it")
                }

            }

        }


    }

}
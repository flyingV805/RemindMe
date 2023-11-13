package kz.flyingv.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import kz.flyingv.remindme.features.reminds.RemindsScreen
import kz.flyingv.remindme.ui.darkUI
import kz.flyingv.remindme.ui.lightUI

class ReminderActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            MaterialTheme(
                colorScheme = if(isSystemInDarkTheme()) {
                    darkUI()
                }else{
                    lightUI()
                }
            ) {
                RemindsScreen()
            }
        }

    }

}
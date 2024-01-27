package kz.flyingv.remindme

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.flyingv.remindme.domain.usecase.CheckSchedulerUseCase
import kz.flyingv.remindme.features.reminds.RemindsScreen
import kz.flyingv.remindme.ui.darkUI
import kz.flyingv.remindme.ui.lightUI
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderActivity: ComponentActivity(), KoinComponent {

    private val checkSchedulerUseCase: CheckSchedulerUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO){ checkSchedulerUseCase() }

        setContent {
            val colors = when {
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
                    if (isSystemInDarkTheme()) dynamicDarkColorScheme(this)
                    else dynamicLightColorScheme(this)
                }
                isSystemInDarkTheme() -> darkUI()
                else -> lightUI()
            }

            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(
                color = colors.primaryContainer
            )

            MaterialTheme(
                colorScheme = colors
            ) {
                RemindsScreen()
            }
        }

    }

}
package kz.flyingv.remindme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
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
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO){ checkSchedulerUseCase() }

        setContent {

            val colors = when {
                /*(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
                    if (isSystemInDarkTheme()) dynamicDarkColorScheme(this)
                    else dynamicLightColorScheme(this)
                }*/
                isSystemInDarkTheme() -> darkUI()
                else -> lightUI()
            }

            MaterialTheme(
                colorScheme = colors
            ) {
                RemindsScreen()
            }
        }

    }

}
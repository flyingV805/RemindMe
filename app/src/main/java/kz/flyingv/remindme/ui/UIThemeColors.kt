package kz.flyingv.remindme.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import kz.flyingv.remindme.R

@Composable
fun lightUI(): ColorScheme {
    return lightColorScheme(
        primary = colorResource(id = R.color.purple_700),
        //primaryVariant = colorResource(id = R.color.purple_500),
        secondary = colorResource(id = R.color.teal_200),
        //secondaryVariant =  colorResource(id = R.color.teal_200),
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        error = Color(0xFFCF6679),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        onError = Color.Black
    )
}

@Composable
fun darkUI(): ColorScheme {

    return darkColorScheme(
        primary = colorResource(id = R.color.purple_700),
        //primaryVariant = colorResource(id = R.color.purple_500),
        secondary = colorResource(id = R.color.teal_200),
        //secondaryVariant =  colorResource(id = R.color.teal_200),
        background = Color(0xFF3b384b),
        surface = Color(0xFF595673),
        error = Color(0xFFCF6679),
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White,
        onError = Color.Black
    )

}
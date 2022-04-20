package kz.flyingv.remindme.ui.iconselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.R
import kz.flyingv.remindme.model.RemindIcon

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    onSelectionChanged: (icon: RemindIcon) -> Unit
){
    val state by remember { mutableStateOf(IconSelectorState()) }

    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ){
        RemindIcon.values().forEach { icon ->
            Spacer(modifier = Modifier.width(4.dp))
            SelectableIcon(
                icon = getIcon(icon),
                isSelected = state.selectIcon == icon,
                onSelect = {
                    state.selectIcon = icon
                    onSelectionChanged(icon)
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}

@Composable
fun getIcon(icon: RemindIcon): Painter{
    return when(icon){
        RemindIcon.Cake -> painterResource(id = R.drawable.ic_avatar_cake)
        RemindIcon.Medicine -> painterResource(id = R.drawable.ic_avatar_medeicine)
        RemindIcon.Officials -> painterResource(id = R.drawable.ic_avatar_officials)
        RemindIcon.Payday -> painterResource(id = R.drawable.ic_avatar_payday)
        RemindIcon.Workout -> painterResource(id = R.drawable.ic_avatar_workout)
    }
}

@Composable
fun SelectableIcon(
    icon: Painter,
    isSelected: Boolean,
    onSelect: () -> Unit,
){
    Box(modifier = Modifier
        .shadow(4.dp, CircleShape)
        .background(
            if (isSelected) {
                Color.Cyan
            } else {
                Color.White
            },
            CircleShape
        )
        .clickable {
            onSelect()
        }
    ){
        Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
    }
}

private class IconSelectorState {
    var selectIcon by mutableStateOf(RemindIcon.Cake)
}

@Preview
@Composable fun IconSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Icon Selector", style = MaterialTheme.typography.caption)

                IconSelector(
                    modifier = Modifier.fillMaxWidth(),
                    onSelectionChanged = {}
                )
            }
        }
    }
}
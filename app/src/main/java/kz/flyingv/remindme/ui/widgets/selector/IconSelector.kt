package kz.flyingv.remindme.ui.widgets.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.R
import kz.flyingv.remindme.domain.entity.ReminderIcon

@Composable
fun IconSelector(
    modifier: Modifier = Modifier,
    currentSelect: ReminderIcon,
    onSelectionChanged: (icon: ReminderIcon) -> Unit
){

    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ){
        ReminderIcon.values().forEach { icon ->
            Spacer(modifier = Modifier.width(4.dp))
            SelectableIcon(
                icon = getIcon(icon),
                isSelected = currentSelect == icon,
                onSelect = {
                    onSelectionChanged(icon)
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}

@Composable
fun getIcon(icon: ReminderIcon): Painter {
    return when(icon){
        ReminderIcon.Cake -> painterResource(id = R.drawable.ic_avatar_cake)
        ReminderIcon.Medicine -> painterResource(id = R.drawable.ic_avatar_medecine)
        ReminderIcon.Officials -> painterResource(id = R.drawable.ic_avatar_officials)
        ReminderIcon.Payday -> painterResource(id = R.drawable.ic_avatar_payday)
        ReminderIcon.Workout -> painterResource(id = R.drawable.ic_avatar_workout)
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
                colorResource(id = R.color.purple_200)
            } else {
                Color.White
            },
            CircleShape
        )
        .clickable {
            onSelect()
        }
    ){
        Icon(
            painter = icon,
            contentDescription = "",
            modifier = Modifier.padding(12.dp).width(24.dp).height(24.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable fun IconSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Icon Selector", style = MaterialTheme.typography.labelSmall)

                IconSelector(
                    modifier = Modifier.fillMaxWidth(),
                    currentSelect = ReminderIcon.Medicine,
                    onSelectionChanged = {}
                )
            }
        }
    }
}
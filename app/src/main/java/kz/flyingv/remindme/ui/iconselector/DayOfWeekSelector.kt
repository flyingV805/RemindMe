package kz.flyingv.remindme.ui.iconselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    days: List<String> = listOf("Mn", "Tu", "We", "Th", "Fr", "Sa", "Su"),
    onSelectionChanged: (dayOfWeek: Int) -> Unit
){
    val state by remember { mutableStateOf(DayOfWeekState()) }

    Row (
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ){
        days.forEachIndexed { index, nameOfDay ->
            Spacer(modifier = Modifier.width(4.dp))
            DayOfWeekItem(
                name = nameOfDay,
                isSelected = state.selectDayIndex == index,
                onSelect = {
                    state.selectDayIndex = index
                    onSelectionChanged(index)
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}

private class DayOfWeekState {
    var selectDayIndex by mutableStateOf(0)
}

@Composable
fun DayOfWeekItem(
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
){
    Box(modifier = Modifier
        .shadow(4.dp, CircleShape)
        .width(36.dp)
        .aspectRatio(1f)
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
        },
        contentAlignment = Alignment.Center
    ){
        Text(name, modifier = Modifier.padding(4.dp))
        //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
    }
}

@Preview
@Composable fun DayOfWeekSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Week Selector", style = MaterialTheme.typography.caption)

                DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    onSelectionChanged = {}
                )
            }
        }
    }
}
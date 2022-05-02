package kz.flyingv.remindme.ui.widgets.selector

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
import kz.flyingv.remindme.data.model.DayOfWeek
import kz.flyingv.remindme.utils.datetime.DatetimeUtils

@Composable
fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    selectedDay: DayOfWeek,
    onSelectionChanged: (dayOfWeek: DayOfWeek) -> Unit
){
    //val state by remember { mutableStateOf(DayOfWeekState()) }

    Row (
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ){
        DayOfWeek.values().forEach { day ->
            Spacer(modifier = Modifier.width(4.dp))
            DayOfWeekItem(
                name = dayOfWeekName(day),
                isSelected = selectedDay == day,
                onSelect = {
                    //state.selectDayIndex = index
                    onSelectionChanged(day)
                }
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }

}

private fun dayOfWeekName(dayOfWeek: DayOfWeek): String{
    return when(dayOfWeek){
        DayOfWeek.MONDAY -> "Mn"
        DayOfWeek.TUESDAY -> "Tu"
        DayOfWeek.WEDNESDAY -> "We"
        DayOfWeek.THURSDAY -> "Th"
        DayOfWeek.FRIDAY -> "Fr"
        DayOfWeek.SATURDAY -> "Sa"
        DayOfWeek.SUNDAY -> "Su"
    }
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
        .height(36.dp)
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
                    selectedDay = DayOfWeek.MONDAY,
                    onSelectionChanged = {}
                )
            }
        }
    }
}
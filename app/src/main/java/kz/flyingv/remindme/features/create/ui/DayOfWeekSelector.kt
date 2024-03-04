package kz.flyingv.remindme.features.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.R
import kz.flyingv.remindme.domain.entity.DayOfWeek

@Composable
fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    selectedDays: Set<DayOfWeek>,
    onSelectionChanged: (dayOfWeek: DayOfWeek) -> Unit
){

    Row (
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ){
        DayOfWeek.entries.forEach { day ->
            Spacer(modifier = Modifier.width(4.dp))
            DayOfWeekItem(
                name = dayOfWeekName(day),
                isSelected = selectedDays.contains(day),
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
        DayOfWeek.Mon -> "Mn"
        DayOfWeek.Tue -> "Tu"
        DayOfWeek.Wed -> "We"
        DayOfWeek.Thu -> "Th"
        DayOfWeek.Fri -> "Fr"
        DayOfWeek.Sat -> "Sa"
        DayOfWeek.Sun -> "Su"
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
                MaterialTheme.colorScheme.surfaceTint
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            CircleShape
        )
        .clickable {
            onSelect()
        },
        contentAlignment = Alignment.Center
    ){
        Text(name, modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onPrimary)
        //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
    }
}

@Preview
@Composable fun DayOfWeekSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Week Selector", style = MaterialTheme.typography.labelSmall)

                DayOfWeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectedDays = hashSetOf(DayOfWeek.Mon),
                    onSelectionChanged = {}
                )
            }
        }
    }
}
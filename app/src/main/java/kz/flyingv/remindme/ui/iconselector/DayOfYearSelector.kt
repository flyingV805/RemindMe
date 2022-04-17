package kz.flyingv.remindme.ui.iconselector

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DayOfYearSelector(
    modifier: Modifier = Modifier,
    days: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"),
    months: List<String> = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"),
    onSelectionChanged: (day: Int, month: Int) -> Unit
) {

    Row(
        modifier = modifier.padding(16.dp)
    ){
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .weight(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {

            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text("January", modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .weight(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {

            },
            contentAlignment = Alignment.CenterEnd
        ){
            Text("2", modifier = Modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Center)
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
        }

    }

}

private class DayOfYearSelectorState {
    var selectDayIndex by mutableStateOf(0)
    var selectDayMonth by mutableStateOf(0)
}

@Preview
@Composable fun DayOfYearSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Year Selector", style = MaterialTheme.typography.caption)

                DayOfYearSelector(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    onSelectionChanged = {day, month ->  }
                )
            }
        }
    }
}
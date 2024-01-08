package kz.flyingv.remindme.features.create.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.flyingv.remindme.R

@Composable
fun DayOfMonthSelector(
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    selectDay: Int,
    onSelectionChanged: (dayOfMonth: Int) -> Unit
) {

    LazyRow (
        modifier = modifier.padding(16.dp),
        state = scrollState,
        horizontalArrangement = Arrangement.Center
    ) {
        for (day in 1..31) {
            item {
                Spacer(modifier = Modifier.width(4.dp))
                DayOfMonthItem(
                    name = day.toString(),
                    isSelected = selectDay == day,
                    onSelect = {
                        //state.selectDayIndex = it
                        onSelectionChanged(day)
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun DayOfMonthItem(
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
                colorResource(id = R.color.purple_200)
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
        Text(name, modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onPrimary)
        //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
    }
}

@Preview
@Composable
fun DayOfMonthSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day Of Month Selector", style = MaterialTheme.typography.labelSmall)

                DayOfMonthSelector(
                    modifier = Modifier.fillMaxWidth(),
                    selectDay = 0,
                    onSelectionChanged = {}
                )
            }
        }
    }
}
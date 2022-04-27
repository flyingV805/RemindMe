package kz.flyingv.remindme.ui.widgets.iconselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kz.flyingv.remindme.data.model.InstalledApp


@Composable
fun AppSelector(
    modifier: Modifier = Modifier,
    apps: List<InstalledApp> = emptyList(),
    selectedApp: InstalledApp? = null,
    onSelectionChanged: (app: InstalledApp) -> Unit
) {

    val isDropdownExpanded = remember { mutableStateOf(false)}

    Row(
        modifier = modifier.fillMaxWidth().fillMaxHeight().padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ){
        Box(modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .fillMaxHeight()
            .weight(1f)
            .background(Color.White, RoundedCornerShape(8.dp))
            .clickable {
                isDropdownExpanded.value = true
            },
            contentAlignment = Alignment.CenterEnd
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    modifier = Modifier.width(36.dp).height(36.dp),
                    painter = rememberDrawablePainter(drawable = selectedApp?.icon),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = selectedApp?.name ?: "Choose app...",
                    modifier = Modifier.padding(8.dp).weight(1f)
                )
            }
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "", modifier = Modifier.padding(end = 8.dp))
            DropdownMenu(
                modifier = Modifier.height(256.dp),
                expanded = isDropdownExpanded.value,
                onDismissRequest = {
                    isDropdownExpanded.value = false
                }
            ) {
                apps.forEach {
                    DropdownMenuItem(onClick = {
                        onSelectionChanged(it)
                        isDropdownExpanded.value = false
                    }) {
                        Image(
                            modifier = Modifier.width(36.dp).height(36.dp),
                            painter = rememberDrawablePainter(drawable = it.icon),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = it.name, modifier = Modifier.weight(1f))
                    }
                }
            }
            //Icon(icon, "", Modifier.padding(12.dp).width(24.dp).height(24.dp))
        }
    }

}

@Preview
@Composable
fun AppSelectorDemo() {
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Day of Year Selector", style = MaterialTheme.typography.caption)
                AppSelector(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onSelectionChanged = {_ ->  }
                )
            }
        }
    }
}
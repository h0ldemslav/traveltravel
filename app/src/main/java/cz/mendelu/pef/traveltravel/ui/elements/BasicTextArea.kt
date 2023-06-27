package cz.mendelu.pef.traveltravel.ui.elements

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

const val TEXT_AREA_MAX_LINES = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTextArea(
    modifier: Modifier,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = TEXT_AREA_MAX_LINES,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        maxLines = maxLines,
        modifier = modifier,
        label = { if (label != null) Text(text = label) }
    )
}
package cz.mendelu.pef.traveltravel.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CircleProgress(
    topSpacePadding: Int = 0,
    bottomSpacePadding: Int = 0,
    caption: String,
    circleProgressVisibility: Boolean
) {
    if (circleProgressVisibility) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(topSpacePadding.dp))

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = caption,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(start = 2.dp)
            )

            Spacer(modifier = Modifier.height(bottomSpacePadding.dp))
        }
    }
}
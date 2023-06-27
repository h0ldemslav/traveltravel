package cz.mendelu.pef.traveltravel.ui.screens.statistics

import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.extensions.isDark
import cz.mendelu.pef.traveltravel.model.BusinessStatistics
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BasicScaffold
import cz.mendelu.pef.traveltravel.ui.elements.CircleProgress
import cz.mendelu.pef.traveltravel.ui.theme.*
import org.koin.androidx.compose.getViewModel

@Composable
fun StatisticsScreen(
    navigation: NavigationRouter,
    viewModel: StatisticsScreenViewModel = getViewModel()
) {
    val textColors = MaterialTheme.colorScheme.onSurface.toArgb()
    // Workaround to detect if theme is dark or not
    val sliceColors = if (MaterialTheme.colorScheme.isDark()) {
        mutableListOf(
            pieColorLight1.toArgb(),
            pieColorLight2.toArgb(),
            pieColorLight3.toArgb(),
            pieColorLight4.toArgb(),
            pieColorLight5.toArgb()
        )
    } else {
        mutableListOf(
            pieColorDark1.toArgb(),
            pieColorDark2.toArgb(),
            pieColorDark3.toArgb(),
            pieColorDark4.toArgb(),
            pieColorDark5.toArgb()
        )
    }

    var statisticsData by remember { mutableStateOf(viewModel.statisticsData) }
    var pieChartData by remember { mutableStateOf(viewModel.pieChartData) }
    var circleProgressVisibility by remember { mutableStateOf(false) }

    viewModel.state.value.let { state ->
        when (state) {
            StatisticsScreenUIState.Default -> {
                circleProgressVisibility = true
                viewModel.getStatistics()
            }

            StatisticsScreenUIState.Success -> {
                statisticsData = viewModel.statisticsData

                LaunchedEffect(state) {
                    viewModel.setPieChartData(sliceColors, textColors)
                    pieChartData = viewModel.pieChartData
                    circleProgressVisibility = false
                }
            }
        }
    }

    BasicScaffold(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.statistics_screen_name)
    ) {
        StatisticsScreenContent(
            contentPadding = it,
            circleProgressVisibility = circleProgressVisibility,
            data = statisticsData,
            pieData = pieChartData
        )
    }
}

@Composable
fun StatisticsScreenContent(
    contentPadding: PaddingValues,
    circleProgressVisibility: Boolean,
    data: BusinessStatistics,
    pieData: PieData?
) {
    val city =
        if (data.mostVisitedCity != NO_DATA) data.mostVisitedCity
        else stringResource(id = R.string.statistics_text_placeholder)

    val category =
        if (data.favoriteCategory != NO_DATA) data.favoriteCategory
        else stringResource(id = R.string.statistics_text_placeholder)

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (!circleProgressVisibility && pieData != null) {

            StatisticsPieChart(pieData = pieData)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.statistics_number_of_visited_places)
                            + " ${data.totalNumberOfVisitedPlaces}"
                )

                Text(
                    text = stringResource(id = R.string.statistics_most_visited_city)
                            + " $city"
                )

                Text(
                    text = stringResource(id = R.string.statistics_favorite_category)
                            + " $category"
                )

                AnimatedVisibility(visible = pieData.entryCount == 0) {
                    Text(
                        text = stringResource(id = R.string.statistics_screen_empty_stats),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        } else {
            CircleProgress(
                caption = stringResource(id = R.string.circle_bar_indicator_caption_loading),
                circleProgressVisibility = circleProgressVisibility,
                topSpacePadding = 50
            )
        }
    }
}

@Composable
fun StatisticsPieChart(
    pieData: PieData
) {
    val onSurface = MaterialTheme.colorScheme.onSurface.toArgb()
    val textSize = MaterialTheme.typography.bodyMedium.fontSize.value

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp),
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                data.setValueTextSize(12F)

                description.isEnabled = false
                isDrawHoleEnabled = true
                legend.textColor = onSurface
                legend.textSize = textSize
                legend.yOffset = -16F
                legend.xOffset = 8F
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.form = Legend.LegendForm.CIRCLE

                setEntryLabelTextSize(textSize)
                setEntryLabelColor(onSurface)
                setHoleColor(Color.TRANSPARENT)
                setExtraOffsets(0F,10F,20F,0F)
                setUsePercentValues(true)
                setTransparentCircleColor(onSurface)
            }
        },
        update = { view ->
            view.data = pieData
            view.data.setValueTextSize(textSize)
            view.setEntryLabelColor(onSurface)
            view.animateY(3000, Easing.EaseOutBack)
        }
    )
}
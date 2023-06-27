package cz.mendelu.pef.traveltravel.ui.screens.statistics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.database.BusinessesRepository
import cz.mendelu.pef.traveltravel.model.BusinessStatistics
import kotlinx.coroutines.launch

const val NO_DATA = "None"
const val MAX_PIE_CHART_SLICES = 5

class StatisticsScreenViewModel(
    private val businessesRepository: BusinessesRepository
    ) : BaseViewModel()
{
    val state: MutableState<StatisticsScreenUIState> =
        mutableStateOf(StatisticsScreenUIState.Default)

    var statisticsData = (
        BusinessStatistics(
            citiesWithNumberOfVisitedPlaces = mutableListOf(),
            mostVisitedCity = NO_DATA,
            favoriteCategory = NO_DATA,
            totalNumberOfVisitedPlaces = 0
        )
    )

    var pieChartData: PieData? = null

    fun getStatistics() {
        getMostVisitedCity()
        getFavoriteCategory()
    }

    fun setPieChartData(sliceColors: MutableList<Int>, textColors: Int) {
        var entries: List<PieEntry> = statisticsData.citiesWithNumberOfVisitedPlaces.map {
            PieEntry(it.values.first().toFloat(), it.keys.first())
        }

        if (entries.size > MAX_PIE_CHART_SLICES) entries = entries.take(MAX_PIE_CHART_SLICES)

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.colors = sliceColors
        pieDataSet.valueTextColor = textColors
        pieDataSet.valueFormatter = PieChartValueFormatter()

        pieChartData = PieData(pieDataSet)
    }

    private fun getMostVisitedCity() {
        launch {
            businessesRepository.getCitiesWithNumberOfVisitedPlaces().collect {
                var cityName = NO_DATA
                var maximumNumberOfVisitedPlaces = 0
                var totalVisited = 0

                statisticsData.citiesWithNumberOfVisitedPlaces.clear()

                it.forEach { record ->
                    if (record.number_of_visited_places > maximumNumberOfVisitedPlaces) {
                        maximumNumberOfVisitedPlaces = record.number_of_visited_places
                        cityName = record.city ?: cityName
                    }

                    if (record.city != null) {
                        statisticsData.citiesWithNumberOfVisitedPlaces.add(
                            mapOf(record.city to record.number_of_visited_places)
                        )
                    }

                    totalVisited += record.number_of_visited_places
                }

                // Sorting list by visited cities to show only 5 most visited cities on a pie chart
                statisticsData.citiesWithNumberOfVisitedPlaces.sortBy { numberOfCities ->
                    numberOfCities.values.first()
                }

                statisticsData.mostVisitedCity = cityName
                statisticsData.totalNumberOfVisitedPlaces = totalVisited

                state.value = StatisticsScreenUIState.Success
            }
        }
    }

    private fun getFavoriteCategory() {
        launch {
            businessesRepository.getCategoriesWithNumberOfVisitedPlaces().collect {
                var categoryName = NO_DATA
                var maximumNumberOfVisitedPlaces = 0

                it.forEach { record ->
                    if (record.number_of_visited_places > maximumNumberOfVisitedPlaces) {
                        maximumNumberOfVisitedPlaces = record.number_of_visited_places
                        categoryName = record.name
                    }
                }

                statisticsData.favoriteCategory = categoryName

                state.value = StatisticsScreenUIState.Success
            }
        }
    }

}
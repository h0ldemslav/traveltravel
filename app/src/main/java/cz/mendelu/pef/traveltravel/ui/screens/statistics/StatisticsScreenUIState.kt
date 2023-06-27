package cz.mendelu.pef.traveltravel.ui.screens.statistics

sealed class StatisticsScreenUIState {
    object Default : StatisticsScreenUIState()
    object Success : StatisticsScreenUIState()
}

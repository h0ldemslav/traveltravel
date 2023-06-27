package cz.mendelu.pef.traveltravel.ui.activities

import cz.mendelu.pef.traveltravel.model.AppSettings

sealed class MainActivityUIState {
    object Default : MainActivityUIState()
    class Success(val appSettings: AppSettings) : MainActivityUIState()
}

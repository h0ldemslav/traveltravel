package cz.mendelu.pef.traveltravel.ui.screens.placedetail

import cz.mendelu.pef.traveltravel.model.BusinessData

sealed class PlaceDetailScreenUIState {
    object Default : PlaceDetailScreenUIState()
    object Loading : PlaceDetailScreenUIState()
    class Success (val business: BusinessData, val snackbarMessageID: Int? = null) : PlaceDetailScreenUIState()
    class Error(val snackbarMessageID: Int?) : PlaceDetailScreenUIState()
}
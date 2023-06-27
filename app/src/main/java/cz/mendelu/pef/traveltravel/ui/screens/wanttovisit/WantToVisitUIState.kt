package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import cz.mendelu.pef.traveltravel.model.BusinessData

sealed class WantToVisitUIState {
    object Default : WantToVisitUIState()
    class Success(val businesses: List<BusinessData>) : WantToVisitUIState()
    class Error(val messageID: Int) : WantToVisitUIState()
}

package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import cz.mendelu.pef.traveltravel.model.BusinessData

interface WantToVisitActions {
    fun updateBusinessIsVisited(business: BusinessData, isVisited: Boolean)
    fun deleteAllBusinessByCity(city: String)
    fun deleteBusiness(businessData: BusinessData)
}
package cz.mendelu.pef.traveltravel.navigation

import androidx.navigation.NavController

interface NavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToPlaceDetailScreen(id: Long?)
    fun navigateToWantToVisitPlacesScreen(city: String)
    fun navigateToAddEditNoteScreen(id: Long?)
    fun navigateToSettingsScreen()
}
package cz.mendelu.pef.traveltravel.navigation

import androidx.navigation.NavController

class NavigationRouterImp(private val navController: NavController) : NavigationRouter {
    override fun getNavController(): NavController {
        return navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToPlaceDetailScreen(id: Long?) {
        navController.navigate(
            route = SecondaryDestination.PlaceDetailScreen.route + "/" + id
        )
    }

    override fun navigateToWantToVisitPlacesScreen(city: String) {
        navController.navigate(
            route = SecondaryDestination.WantToVisitPlacesScreen.route + "/" + city
        )
    }

    override fun navigateToAddEditNoteScreen(id: Long?) {
        navController.navigate(
            route = SecondaryDestination.AddEditNoteScreen.route + "/" + id
        )
    }

    override fun navigateToSettingsScreen() {
        navController.navigate(route = SecondaryDestination.SettingsScreen.route)
    }
}
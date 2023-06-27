package cz.mendelu.pef.traveltravel.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.traveltravel.model.AppSettings
import cz.mendelu.pef.traveltravel.ui.screens.placedetail.PlaceDetailScreen
import cz.mendelu.pef.traveltravel.ui.screens.search.SearchScreen
import cz.mendelu.pef.traveltravel.ui.screens.settings.SettingsScreen
import cz.mendelu.pef.traveltravel.ui.screens.statistics.StatisticsScreen
import cz.mendelu.pef.traveltravel.ui.screens.wanttovisit.AddEditNoteScreen
import cz.mendelu.pef.traveltravel.ui.screens.wanttovisit.WantToVisitCitiesScreen
import cz.mendelu.pef.traveltravel.ui.screens.wanttovisit.WantToVisitPlacesScreen

// NavArgument default values
    const val emptyID: Long = -1L
    const val otherCity: String = "Other"
//

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: NavigationRouter = remember { NavigationRouterImp(navController) },
    startDestination: String,
    appSettings: AppSettings,
    onAppSettingsChange: (AppSettings) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        searchGraph(navigation = navigation)

        wantToVisitGraph(navigation = navigation)

        composable(route = PrimaryDestination.StatisticsScreen.route) {
            StatisticsScreen(navigation = navigation)
        }

        composable(route = SecondaryDestination.SettingsScreen.route) {
            SettingsScreen(
                navigation = navigation,
                appSettings = appSettings,
                onAppSettingsChange = onAppSettingsChange
            )
        }
    }
}

fun NavGraphBuilder.wantToVisitGraph(navigation: NavigationRouter) {
    navigation(
        startDestination = SecondaryDestination.WantToVisitCitiesScreen.route,
        route = PrimaryDestination.WantToVisitScreen.route
    ) {
        composable(route = SecondaryDestination.WantToVisitCitiesScreen.route) {
            WantToVisitCitiesScreen(navigation = navigation)
        }

        composable(
            route = SecondaryDestination.WantToVisitPlacesScreen.route + "/{city}",
            arguments = listOf(
                navArgument("city") {
                    type = NavType.StringType
                    defaultValue = otherCity
                }
            )
        ) {
            val cityString = it.arguments?.getString("city")
            WantToVisitPlacesScreen(navigation = navigation, city = cityString ?: otherCity)
        }

        composable(
            route = SecondaryDestination.AddEditNoteScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.LongType
                    defaultValue = emptyID
                }
            )
        ) {
            val id = it.arguments?.getLong("id")
            AddEditNoteScreen(navigation = navigation, id = if (id != emptyID) id else null)
        }
    }
}

fun NavGraphBuilder.searchGraph(navigation: NavigationRouter) {
    navigation(
        startDestination = SecondaryDestination.SearchScreenSearch.route,
        route = PrimaryDestination.SearchScreen.route
    ) {
        composable(route = SecondaryDestination.SearchScreenSearch.route) {
            SearchScreen(navigation = navigation)
        }

        composable(
            route = SecondaryDestination.PlaceDetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.LongType
                    defaultValue = emptyID
                }
            )
        ) {
            val id = it.arguments?.getLong("id")
            PlaceDetailScreen(navigation = navigation, id = if (id != emptyID) id else null)
        }
    }
}
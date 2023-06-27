package cz.mendelu.pef.traveltravel.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.Hiking
import androidx.compose.ui.graphics.vector.ImageVector
import cz.mendelu.pef.traveltravel.R

// For bottom navbar
sealed class PrimaryDestination(
    val route: String,
    val titleID: Int,
    val icon: ImageVector
) {
    object SearchScreen : PrimaryDestination(
        route = "search_screen",
        titleID = R.string.search_screen_name,
        icon = Icons.Default.TravelExplore
    )

    object WantToVisitScreen : PrimaryDestination(
        route = "want_to_visit_screen",
        titleID = R.string.want_to_visit_screen_name,
        icon = Icons.Outlined.Hiking
    )

    object StatisticsScreen : PrimaryDestination(
        route = "statistics_screen",
        titleID = R.string.statistics_screen_name,
        icon = Icons.Outlined.DonutLarge
    )
}

// For other things except bottom navbar
sealed class SecondaryDestination(val route: String) {
    object SearchScreenSearch : SecondaryDestination(route = "search_screen_search")
    object PlaceDetailScreen : SecondaryDestination(route = "place_detail_screen")
    object WantToVisitCitiesScreen : SecondaryDestination(route = "want_to_visit_cities_screen")
    object WantToVisitPlacesScreen : SecondaryDestination(route = "want_to_visit_places_screen")
    object AddEditNoteScreen : SecondaryDestination(route = "add_edit_note_screen")
    object SettingsScreen : SecondaryDestination(route = "settings_screen")
}
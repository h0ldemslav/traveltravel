package cz.mendelu.pef.traveltravel.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(
    navigation: NavigationRouter
) {
    val navController = navigation.getNavController()
    val items = listOf(
        PrimaryDestination.StatisticsScreen,
        PrimaryDestination.SearchScreen,
        PrimaryDestination.WantToVisitScreen
    )

    BottomNavigation(backgroundColor = MaterialTheme.colorScheme.surface) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            BottomNavigationItem(
                icon = { Icon(
                    imageVector = screen.icon,
                    contentDescription = null,
                    tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                ) },
                label = { Text(
                    text = stringResource(id = screen.titleID),
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge
                ) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
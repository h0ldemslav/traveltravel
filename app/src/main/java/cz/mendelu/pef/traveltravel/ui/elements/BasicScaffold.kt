package cz.mendelu.pef.traveltravel.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cz.mendelu.pef.traveltravel.navigation.BottomNavBar
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScaffold(
    navigation: NavigationRouter,
    topBarTitle: String,
    topBarAction: () -> Unit = { navigation.navigateToSettingsScreen() },
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
   Scaffold(
       topBar = {
           TopAppBar(
               title = { Text(text = topBarTitle) },
               actions = {
                   IconButton(onClick = topBarAction) {
                       Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
                   }
               }
           )
       },
       bottomBar = { BottomNavBar(navigation = navigation) }
   ) {
        content(it)
   }
}
package cz.mendelu.pef.traveltravel.ui.elements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cz.mendelu.pef.traveltravel.navigation.BottomNavBar
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackArrowScreen(
    navigation: NavigationRouter,
    topBarTitle: String,
    topBarActionIcon: ImageVector? = Icons.Default.MoreVert,
    topBarAction: () -> Unit = {},
    bottomBar: @Composable () -> Unit = { BottomNavBar(navigation = navigation) },
    snackbarHostState: SnackbarHostState? = null,
    onArrowClick: () -> Unit,
    content: @Composable (contentPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = topBarTitle) },
                navigationIcon = {
                    IconButton(onClick = { onArrowClick() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    if (topBarActionIcon != null) {
                        IconButton(onClick = topBarAction) {
                            Icon(imageVector = topBarActionIcon, contentDescription = "")
                        }
                    }
                }
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            if (snackbarHostState != null) {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        snackbarData = it
                    )
                }
            }
        }
    ) {
        content(it)
    }
}
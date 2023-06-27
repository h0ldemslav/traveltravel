package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BasicScaffold
import cz.mendelu.pef.traveltravel.ui.elements.ListRowIcon
import cz.mendelu.pef.traveltravel.ui.elements.PlaceholderInfo
import org.koin.androidx.compose.getViewModel

@Composable
fun WantToVisitCitiesScreen(
    navigation: NavigationRouter,
    viewModel: WantToVisitViewModel = getViewModel()
) {
    val cities = remember {
        mutableStateListOf<String>()
    }

    viewModel.state.value.let { state ->
        when (state) {
            WantToVisitUIState.Default -> {
                viewModel.getAllBusinesses()
            }

            is WantToVisitUIState.Success -> {
                cities.clear()
                cities.addAll(viewModel.getAllCitiesFromBusinesses(state.businesses))
            }

            is WantToVisitUIState.Error -> {

            }
        }
    }

    BasicScaffold(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.want_to_visit_screen_name)
    ) {
        WantToVisitCitiesScreenContent(
            contentPadding = it,
            cities = cities,
            actions = viewModel,
            navigation = navigation
        )
    }
}

@Composable
fun WantToVisitCitiesScreenContent(
    contentPadding: PaddingValues,
    cities: MutableList<String>,
    actions: WantToVisitActions,
    navigation: NavigationRouter
) {
    if (cities.size > 0) {
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(top = 35.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    actions = actions,
                    navigation = navigation
                )
            }
        }
    } else {
        Column(modifier = Modifier.padding(contentPadding)) {
            PlaceholderInfo(
                vectorResourceID = R.drawable.undraw_void,
                textResourceID = R.string.want_to_visit_cities_empty_title,
                supportingTextResourceID = R.string.want_to_visit_cities_empty_supporting_text
            )
        }
    }
}

@Composable
fun CityRow(
    city: String,
    actions: WantToVisitActions,
    navigation: NavigationRouter
) {
    var isDeleteDialogOpened by remember { mutableStateOf(false) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        .clickable { navigation.navigateToWantToVisitPlacesScreen(city) }
    ) {
        ListRowIcon(labelLetter = city[0].toString())

        Text(
            text = city,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp)
                .weight(2f)
        )

        IconButton(
            onClick = { isDeleteDialogOpened = true },
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
    
    if (isDeleteDialogOpened) {
        DeleteCityPlacesDialog(
            onDismissRequest = { isDeleteDialogOpened = false },
            onDeleteButtonClick = {
                isDeleteDialogOpened = false
                actions.deleteAllBusinessByCity(city)
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteCityPlacesDialog(
    onDismissRequest: () -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dialog_remove_city_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = stringResource(id = R.string.dialog_remove_city_content),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = onDeleteButtonClick) {
                        Text(text = stringResource(id = R.string.dialog_textbutton_remove))
                    }

                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.dialog_textbutton_cancel))
                    }
                }
            }
        }
    }
}
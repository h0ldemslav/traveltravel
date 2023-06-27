package cz.mendelu.pef.traveltravel.ui.screens.wanttovisit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.extensions.getImageRatingStarsID
import cz.mendelu.pef.traveltravel.extensions.isDark
import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BackArrowScreen
import cz.mendelu.pef.traveltravel.ui.elements.ListRowIcon
import org.koin.androidx.compose.getViewModel

@Composable
fun WantToVisitPlacesScreen(
    navigation: NavigationRouter,
    viewModel: WantToVisitViewModel = getViewModel(),
    city: String
) {
    val businesses = remember {
        mutableStateListOf<BusinessData>()
    }

    viewModel.state.value.let { state ->
        when (state) {
            WantToVisitUIState.Default -> {
                viewModel.getAllBusinessesByCity(city)
            }

            is WantToVisitUIState.Success -> {
                businesses.clear()
                businesses.addAll(state.businesses)
            }

            is WantToVisitUIState.Error -> {

            }
        }
    }

    BackArrowScreen(
        navigation = navigation,
        topBarTitle = city,
        topBarAction = { navigation.navigateToSettingsScreen() },
        onArrowClick = { navigation.returnBack() }
    ) {
        WantToVisitPlacesScreenContent(
            contentPadding = it,
            businesses = businesses,
            navigation = navigation,
            actions = viewModel
        )
    }
}

@Composable
fun WantToVisitPlacesScreenContent(
    contentPadding: PaddingValues,
    businesses: MutableList<BusinessData>,
    navigation: NavigationRouter,
    actions: WantToVisitActions
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(top = 15.dp)
    ) {

        Text(
            text = stringResource(id = R.string.results_supporting_text),
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        LazyColumn {
            items(businesses) { business ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigation.navigateToPlaceDetailScreen(business.id)  }
                        .padding(top = 22.dp),
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    ListRowIcon(labelLetter = business.name[0].toString())

                    ListRow(
                        business = business,
                        actions = actions,
                        navigation = navigation
                    )

                    IconButton(
                        onClick = { actions.deleteBusiness(business) },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun ListRow(
    business: BusinessData,
    navigation: NavigationRouter,
    actions: WantToVisitActions
) {
    val visibleCharacters = 24
    val reviewsLabel = stringResource(id = R.string.card_row_reviews)
    val priceLabel = stringResource(id = R.string.card_row_price)

    Column(
        modifier = Modifier
            .width(295.dp)
            .padding(start = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (business.name.length > visibleCharacters)
                    business.name.slice(0..visibleCharacters) + ".."
                else
                    business.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (business.is_visited) Color.Green else MaterialTheme.colorScheme.onSurface,
                textDecoration = if (business.is_visited) TextDecoration.LineThrough else TextDecoration.None
            )
        }

        Row {
            Text(
                text = business.reviewCount + " $reviewsLabel" + ", $priceLabel - ${business.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 2.dp)
            )
        }

        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .padding(start = 1.dp)
                    .padding(end = 20.dp),
                painter = painterResource(id = business.rating.getImageRatingStarsID()),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .width(55.dp),
                // Workaround to detect if theme is dark or not
                painter = if (MaterialTheme.colorScheme.isDark())
                    painterResource(id = R.drawable.yelp_logo_dark_bg)
                else painterResource(id = R.drawable.yelp_logo),
                contentDescription = null
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TextButton(
                onClick = { navigation.navigateToAddEditNoteScreen(business.id) },
                contentPadding = PaddingValues.Absolute(left = 0.dp)
            ) {
                if (business.userNote != null) {
                    Text(text = stringResource(id = R.string.button_label_note_view))
                } else {
                    Text(text = stringResource(id = R.string.button_label_note_add))
                }
            }

            TextButton(
                modifier = Modifier.width(160.dp),
                onClick = { actions.updateBusinessIsVisited(business, !business.is_visited) }
            ) {
                Text(text = stringResource(id = R.string.button_label_business_visited))
            }
        }

    }
}
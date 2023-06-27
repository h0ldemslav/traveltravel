package cz.mendelu.pef.traveltravel.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.extensions.getImageRatingStarsID
import cz.mendelu.pef.traveltravel.extensions.isAtBottom
import cz.mendelu.pef.traveltravel.extensions.isDark
import cz.mendelu.pef.traveltravel.model.Business
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.navigation.emptyID
import cz.mendelu.pef.traveltravel.services.api.INITIAL_OFFSET
import cz.mendelu.pef.traveltravel.ui.elements.*
import cz.mendelu.pef.traveltravel.utils.LocaleUtils
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(
    navigation: NavigationRouter,
    viewModel: SearchScreenViewModel = getViewModel()
) {
    var searchScreenData by remember { mutableStateOf(viewModel.data) }
    val placeholderData by remember { mutableStateOf(viewModel.placeholderData) }
    var circleProgressVisibility by remember { mutableStateOf(false) }
    val lazyListState: LazyListState = rememberLazyListState()

    viewModel.state.value.let { state ->
        when (state) {
            SearchScreenUIState.Default -> {}

            SearchScreenUIState.DataChanged -> {
                searchScreenData = viewModel.data
                viewModel.state.value = SearchScreenUIState.Default
            }

            is SearchScreenUIState.InitialLoading -> {
                LaunchedEffect(state) {
                    if (viewModel.businesses.isNotEmpty()) {
                        // Needed to start LazyColumn items on top when new data are fetched
                        lazyListState.scrollToItem(0)
                        viewModel.businesses.clear()
                    }

                    val locale = LocaleUtils.getFormattedLocaleForAPI()

                    viewModel.sendAPIRequest(offset = INITIAL_OFFSET, locale = locale)
                    circleProgressVisibility = true
                }
            }

            SearchScreenUIState.Success -> {
                circleProgressVisibility = false
                viewModel.state.value = SearchScreenUIState.Default
            }

            is SearchScreenUIState.Paginating -> {
                LaunchedEffect(state) {
                    circleProgressVisibility = true

                    val locale = LocaleUtils.getFormattedLocaleForAPI()

                    viewModel.sendAPIRequest(offset = state.offset, locale = locale)
                }
            }

            SearchScreenUIState.PageEnd -> {
                circleProgressVisibility = false
            }

            SearchScreenUIState.Error -> {
                circleProgressVisibility = false
            }
        }
    }

    BasicScaffold(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.app_name)
    ) {
        SearchScreenContent(
            contentPadding = it,
            searchScreenData = searchScreenData,
            actions = viewModel,
            businesses = viewModel.businesses,
            circleProgressVisibility = circleProgressVisibility,
            placeholderData = placeholderData,
            lazyListState = lazyListState,
            navigation = navigation
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreenContent(
    contentPadding: PaddingValues,
    searchScreenData: SearchScreenData,
    actions: SearchScreenActions,
    businesses: List<Business>,
    circleProgressVisibility: Boolean,
    placeholderData: SearchScreenPlaceholderData,
    lazyListState: LazyListState,
    navigation: NavigationRouter
) {
    val context = LocalContext.current

    val maxNumOfCharacters = 256
    val inputCategoryOptions = searchScreenData.getAllCategoryResourceIDs()
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(inputCategoryOptions[0]) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val isAtBottom = lazyListState.isAtBottom()

    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            actions.loadNextPage(businesses.size + 1)
        }
    }

    Column(
        modifier = Modifier
            .padding(contentPadding)
            .padding(horizontal = 14.dp)
            .padding(top = 35.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchTextField(
            value = searchScreenData.name,
            label = context.getString(R.string.search_name_textfield_label),
            onValueChange = {
                if (it.length <= maxNumOfCharacters) actions.updateNameFieldValue(it)
            },
            errorMessage =
            if (searchScreenData.searchError == R.string.search_textfield_name_error)
                context.getString(R.string.search_textfield_name_error)
            else "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                }
            )
        )

        DropDown(
            label = stringResource(id = R.string.search_categories_textfield_label),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            selectedCategory = context.getString(selectedCategory),
            onDismissRequest = { expanded = false }
        ) {
            inputCategoryOptions.forEach { selected ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = context.getString(selected),
                            style = MaterialTheme.typography.bodyLarge
                        )
                   },
                    onClick = {
                        selectedCategory = selected
                        expanded = false
                        actions.updateCategoryFieldValue(selected)
                    }
                )
            }
        }

        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            SearchPriceCheckBoxes(
                actions = actions,
                prices = searchScreenData.prices
            )

            Text(
                text = if (searchScreenData.searchError != R.string.search_price_checkbox_error)
                    context.getString(R.string.search_price_checkbox_hint)
                else context.getString(R.string.search_price_checkbox_error),
                style = MaterialTheme.typography.labelMedium,
                color = if (searchScreenData.searchError != R.string.search_price_checkbox_error) Color.Gray else Color.Red,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        OutlinedButton(onClick = {
            keyboardController?.hide()
            focusManager.clearFocus(true)
            actions.validateBeforeSearch()
        }) {
            Text(
                text = context.getString(R.string.search_screen_name),
                modifier = Modifier.padding(horizontal = 10.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600
            )
        }


        if (businesses.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            ) {
                Text(
                    text = context.getString(R.string.search_screen_results),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 1.dp)
                )

                Text(
                    text = context.getString(R.string.results_supporting_text),
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            LazyColumn(state = lazyListState) {
                items(businesses, key = { business ->
                    business.id
                }) { business ->
                    ListRow(
                        business = business,
                        onRowClick = {
                            val selectedCategoryString = context.getString(selectedCategory)
                            actions.saveToDataStore(business, selectedCategoryString)
                            navigation.navigateToPlaceDetailScreen(emptyID)
                        }
                    )
                }

                item {
                    CircleProgress(
                        topSpacePadding = 20,
                        bottomSpacePadding = 8,
                        caption = context.getString(R.string.circle_bar_indicator_caption_loading),
                        circleProgressVisibility = circleProgressVisibility
                    )
                }
            }

        } else if (
            placeholderData.imageID != null &&
            placeholderData.textID != null
        ) {
            PlaceholderInfo(
                vectorResourceID = placeholderData.imageID!!,
                textResourceID = placeholderData.textID!!,
                supportingTextResourceID = placeholderData.supportingTextID
            )
        }

        CircleProgress(
            topSpacePadding = 20,
            caption = context.getString(R.string.circle_bar_indicator_caption_searching),
            circleProgressVisibility = circleProgressVisibility
        )
    }
}

@Composable
fun SearchPriceCheckBoxes(
    actions: SearchScreenActions,
    prices: MutableMap<SearchPrice, Boolean>
) {
    var moneySign = "$"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (price in prices.keys) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = moneySign)

                prices[price]?.let { p ->
                    Checkbox(
                        checked = p,
                        onCheckedChange = {
                            actions.updatePriceFieldValue(price, it)
                        }
                    )
                }

                moneySign += "$"
            }
        }
    }
}

@Composable
fun ListRow(
    business: Business,
    onRowClick: () -> Unit
) {
    val visibleCharacters = 21
    val reviewsLabel = stringResource(id = R.string.card_row_reviews)
    val priceLabel = stringResource(id = R.string.card_row_price)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .height(88.dp)
            .clickable { onRowClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(64.dp)
                .width(114.dp)
                .align(Alignment.CenterVertically)
        ) {
            if (business.image_url.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(business.image_url)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.undraw_people),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = if (business.name.length > visibleCharacters)
                    business.name.slice(0..visibleCharacters) + ".."
                else
                    business.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = business.review_count + " $reviewsLabel" + ", $priceLabel - ${business.price}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(top = 2.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(end = 20.dp)
                        .padding(start = 1.dp),
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
        }

    }
}

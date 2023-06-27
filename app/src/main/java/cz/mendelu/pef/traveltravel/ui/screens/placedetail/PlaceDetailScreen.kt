package cz.mendelu.pef.traveltravel.ui.screens.placedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.model.BusinessData
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BackArrowScreen
import cz.mendelu.pef.traveltravel.ui.elements.CircleProgress
import cz.mendelu.pef.traveltravel.utils.LocaleUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

const val ONE_DAY_IN_MILLISECONDS: Long = 86400000

@Composable
fun PlaceDetailScreen(
    navigation: NavigationRouter,
    viewModel: PlaceDetailScreenViewModel = getViewModel(),
    id: Long?
) {
    var business by remember { mutableStateOf<BusinessData?>(null) }
    var circleProgressVisibility by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

    viewModel.state.value.let { state ->
        when(state) {
            PlaceDetailScreenUIState.Default -> {}

            PlaceDetailScreenUIState.Loading -> {
                LaunchedEffect(state) {
                    circleProgressVisibility = true

                    if (id != null) {
                        viewModel.getBusinessByIDFromDatabase(id)
                    } else {
                        viewModel.getBusinessRecordFromStore()
                    }
                }
            }

            is PlaceDetailScreenUIState.Success -> {
                circleProgressVisibility = false
                business = state.business

                // Refreshing data every 24 hours due to API policy
                val expired = business!!.whenAdded + ONE_DAY_IN_MILLISECONDS
                val currentTime = System.currentTimeMillis()

                if (id != null && currentTime >= expired) {
                    LaunchedEffect(state) {
                        circleProgressVisibility = true

                        val locale = LocaleUtils.getFormattedLocaleForAPI()

                        viewModel.updateBusinessData(business!!, locale = locale)
                    }
                }

                if (state.snackbarMessageID != null) {
                    val message = stringResource(id = state.snackbarMessageID)

                    LaunchedEffect(state) {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }
            }

            is PlaceDetailScreenUIState.Error -> {
                circleProgressVisibility = false
                val message = stringResource(id = state.snackbarMessageID ?: R.string.wildcard_error)

                LaunchedEffect(state) {
                    snackbarHostState.showSnackbar(message = message)
                }
            }
        }
    }

    BackArrowScreen(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.place_detail_screen_name),
        topBarActionIcon = null,
        bottomBar = {},
        onArrowClick = { navigation.returnBack() },
        snackbarHostState = snackbarHostState
    ) {
        PlaceDetailScreenContent(
            contentPadding = it,
            uriHandler = uriHandler,
            business = business,
            circleProgressVisibility = circleProgressVisibility,
            actions = viewModel,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun PlaceDetailScreenContent(
    contentPadding: PaddingValues,
    uriHandler: UriHandler,
    business: BusinessData?,
    circleProgressVisibility: Boolean,
    actions: PlaceDetailScreenActions,
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val snackbarMessageAddedToWantToVisit = stringResource(id = R.string.place_detail_snackbar_message_saved)

    if (!circleProgressVisibility) {

        if (business != null) {
            val price = if (business.price != null) " | ${business.price}" else ""

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                GMap(
                    location = LatLng(
                        business.latitude,
                        business.longitude
                    )
                )

                Text(
                    text = business.name + price,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.place_detail_phone) + " ${business.phone}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(id = R.string.place_detail_street) + " ${business.address}",
                    style = MaterialTheme.typography.bodyMedium
                )


                Text(
                    text = stringResource(id = R.string.place_detail_coordinates) + " ${business.longitude}" +
                            ", " + business.latitude,
                    style = MaterialTheme.typography.bodyMedium
                )

                AnnotatedClickableText(uri = business.url, uriHandler = uriHandler)

                // If business id (not remote_id) is null,
                // when it was temporary saved to DataStore.
                // Thus the Want to visit button is shown
                if (business.id == null) {
                    OutlinedButton(
                        modifier = Modifier.padding(top = 8.dp),
                        onClick = {
                            actions.addBusinessToWantToVisit(business)
                            scope.launch {
                                snackbarHostState.showSnackbar(message = snackbarMessageAddedToWantToVisit)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.want_to_visit_screen_name),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

    } else {
        CircleProgress(
            topSpacePadding = 100,
            caption = stringResource(id = R.string.circle_bar_indicator_caption_loading),
            circleProgressVisibility = circleProgressVisibility
        )
    }
}

@Composable
fun GMap(
    location: LatLng
) {
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                location.latitude,
                location.longitude
            ), 18f
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(top = 25.dp)
            .padding(bottom = 5.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().border(BorderStroke(1.dp, Color.Black)),
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapLoaded = {
                cameraPositionState.move(CameraUpdateFactory.newLatLng((location)))
            }
        ) {
            Marker(state = MarkerState(position = location))
        }
    }

}

@Composable
fun AnnotatedClickableText(
    uri: String,
    uriHandler: UriHandler
) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.hyperlink_text_start) + " ")

        pushStringAnnotation(tag = "URL", annotation = uri)
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textDecoration = TextDecoration.Underline
        )) {
            append(stringResource(R.string.hyperlink_text_end))
        }

        pop()
    }
    
    ClickableText(
        text = annotatedString,
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
        onClick = {
        annotatedString
            .getStringAnnotations("URL", it, it)
            .firstOrNull()?.let { stringAnnotation ->
                uriHandler.openUri(stringAnnotation.item)
            }
    })
}

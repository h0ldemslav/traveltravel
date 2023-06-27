package cz.mendelu.pef.traveltravel.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cz.mendelu.pef.traveltravel.model.SETTING_FONT_SIZE_LARGE
import cz.mendelu.pef.traveltravel.model.SETTING_SCREEN_WANT_TO_VISIT
import cz.mendelu.pef.traveltravel.navigation.PrimaryDestination
import cz.mendelu.pef.traveltravel.navigation.NavGraph
import cz.mendelu.pef.traveltravel.ui.theme.CustomTypographyLarge
import cz.mendelu.pef.traveltravel.ui.theme.TravelTravelTheme
import cz.mendelu.pef.traveltravel.ui.theme.Typography
import cz.mendelu.pef.traveltravel.utils.LocaleUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var appSettings by remember { mutableStateOf(viewModel.appSettings) }

            viewModel.state.value.let { state ->
                when (state) {
                    MainActivityUIState.Default -> {
                        viewModel.getAppSettings()
                    }

                    is MainActivityUIState.Success -> {
                        appSettings = state.appSettings
                    }
                }
            }

            TravelTravelTheme(
                darkTheme = appSettings.darkTheme,
                typography = if (appSettings.fontSize == SETTING_FONT_SIZE_LARGE) {
                    CustomTypographyLarge
                } else {
                    Typography
                }
            ) {
                LocaleUtils.setLocale(LocalContext.current, appSettings.language)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        startDestination =
                        if (appSettings.defaultScreenRoute == SETTING_SCREEN_WANT_TO_VISIT) {
                            PrimaryDestination.WantToVisitScreen.route
                        } else {
                            PrimaryDestination.SearchScreen.route
                        },
                        appSettings = appSettings.copy(),
                        onAppSettingsChange = { appSettings = it }
                    )
                }
            }
        }
    }
}
package cz.mendelu.pef.traveltravel.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.traveltravel.BuildConfig
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.extensions.isDark
import cz.mendelu.pef.traveltravel.extensions.sendMail
import cz.mendelu.pef.traveltravel.model.AppPreference
import cz.mendelu.pef.traveltravel.model.AppSettings
import cz.mendelu.pef.traveltravel.navigation.NavigationRouter
import cz.mendelu.pef.traveltravel.ui.elements.BackArrowScreen
import cz.mendelu.pef.traveltravel.ui.theme.*
import org.koin.androidx.compose.getViewModel

const val APP_VERSION_NAME = BuildConfig.VERSION_NAME
const val CONTACT_EMAIL = "xastapen@mendelu.cz"
const val EMAIL_SUBJECT = "TravelTravel: feedback"

@Composable
fun SettingsScreen(
    navigation: NavigationRouter,
    viewModel: SettingsScreenViewModel = getViewModel(),
    appSettings: AppSettings,
    onAppSettingsChange: (AppSettings) -> Unit
) {
    val appPreferences = remember { mutableStateListOf<AppPreference>() }
    var currentPreference by remember { mutableStateOf(viewModel.preferences[0]) }

    viewModel.state.value.let { state ->
        when (state) {
            SettingsScreenUIState.Default -> {
                viewModel.setPreferences(appSettings)
            }

            SettingsScreenUIState.Success -> {
                appPreferences.clear()
                appPreferences.addAll(viewModel.preferences)
            }

            SettingsScreenUIState.PreferenceChanged -> {
                LaunchedEffect(state) {
                    val newAppSettings = viewModel.updateAppSettings(appSettings, currentPreference.selectedOptionID)

                    onAppSettingsChange(newAppSettings)
                    viewModel.saveAppSettingsToLocalStore(newAppSettings)
                }
            }

            SettingsScreenUIState.UpdatedLocalStore -> {}
        }
    }

    BackArrowScreen(
        navigation = navigation,
        topBarTitle = stringResource(id = R.string.settings_screen_title),
        topBarActionIcon = null,
        bottomBar = {},
        onArrowClick = { navigation.returnBack() }
    ) {
        SettingScreenContent(
            contentPadding = it,
            preferences = appPreferences,
            currentPreference = currentPreference,
            onCurrentPreferenceChange = { curPref -> currentPreference = curPref },
            actions = viewModel
        )
    }
}

@Composable
fun SettingScreenContent(
    contentPadding: PaddingValues,
    preferences: List<AppPreference>,
    currentPreference: AppPreference,
    onCurrentPreferenceChange: (AppPreference) -> Unit,
    actions: SettingsScreenActions
) {
    val context = LocalContext.current

    val generalSectionColor =
        if (MaterialTheme.colorScheme.isDark()) generalSectionColorDark
        else generalSectionColorLight

    val selectedOptionColor = MaterialTheme.colorScheme.secondary

    var isSettingsDialogOpened by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.settings_general_section),
            color = generalSectionColor,
            fontWeight = FontWeight.W600
        )

        preferences.forEach {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCurrentPreferenceChange(it)
                        isSettingsDialogOpened = true
                    }
                    .padding(vertical = 4.dp)
            ) {
                Text(text = stringResource(id = it.nameID))

                Text(
                    text = stringResource(id = it.selectedOptionID),
                    color = selectedOptionColor
                )
            }
        }

        Text(
            text = stringResource(id = R.string.settings_send_feedback),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { context.sendMail(CONTACT_EMAIL, EMAIL_SUBJECT) }
                .padding(vertical = 4.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.application_version_name_title) + " $APP_VERSION_NAME",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isSettingsDialogOpened) {
            SettingsDialog(
                currentPreference = currentPreference,
                onDismissRequest = { isSettingsDialogOpened = false },
                onOptionClick = { name, option ->
                    actions.updatePreference(name, option)
                    isSettingsDialogOpened = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    currentPreference: AppPreference,
    onOptionClick: (Int, Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = currentPreference.nameID),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                currentPreference.optionIDs.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOptionClick(currentPreference.nameID, option) }
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = option == currentPreference.selectedOptionID,
                            onClick = null
                        )

                        Text(
                            text = stringResource(id = option),
                            modifier = Modifier.padding(start = 4.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(id = R.string.dialog_textbutton_cancel),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

            }

        }

    }
}
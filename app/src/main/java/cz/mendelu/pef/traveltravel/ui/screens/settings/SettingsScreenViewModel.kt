package cz.mendelu.pef.traveltravel.ui.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import cz.mendelu.pef.traveltravel.R
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.localstore.LocalStoreManager
import cz.mendelu.pef.traveltravel.model.*
import kotlinx.coroutines.launch

class SettingsScreenViewModel(
    private val localStoreManager: LocalStoreManager
) : BaseViewModel(), SettingsScreenActions {

    val state: MutableState<SettingsScreenUIState> =
        mutableStateOf(SettingsScreenUIState.Default)

    // Initial preferences
    var preferences = mutableListOf(
        AppPreference(
            nameID = R.string.settings_app_language,
            optionIDs = listOf(
                R.string.settings_app_language_english,
                R.string.settings_app_language_czech
            ),
            selectedOptionID = R.string.settings_app_language_english
        ),

        AppPreference(
            nameID = R.string.settings_app_theme,
            optionIDs = listOf(
                R.string.settings_app_theme_light,
                R.string.settings_app_theme_dark
            ),
            selectedOptionID = R.string.settings_app_theme_light
        ),

        AppPreference(
            nameID = R.string.settings_default_screen,
            optionIDs = listOf(
                R.string.settings_default_screen_search,
                R.string.settings_default_screen_want_to_visit
            ),
            selectedOptionID = R.string.settings_default_screen_search
        ),

        AppPreference(
            nameID = R.string.settings_default_font_size,
            optionIDs = listOf(
                R.string.settings_default_font_size_small,
                R.string.settings_default_font_size_large
            ),
            selectedOptionID = R.string.settings_default_font_size_small
        )
    )

    fun setPreferences(appSettings: AppSettings) {
        if (appSettings.selectedOptionIDs.size == preferences.size) {
            for (index in appSettings.selectedOptionIDs.indices) {
                preferences[index].selectedOptionID =
                    appSettings.selectedOptionIDs[index]
            }
        }

        state.value = SettingsScreenUIState.Success
    }

    fun updateAppSettings(appSettings: AppSettings, selectedOptionID: Int): AppSettings {
        // I really don't know how to refactor this...
        when (selectedOptionID) {
            R.string.settings_app_language_czech -> { appSettings.language = SETTING_LANG_CS }

            R.string.settings_app_language_english -> { appSettings.language = SETTING_LANG_EN }

            R.string.settings_app_theme_light -> { appSettings.darkTheme = false }

            R.string.settings_app_theme_dark -> { appSettings.darkTheme = true }

            R.string.settings_default_screen_search -> { appSettings.defaultScreenRoute = SETTING_SCREEN_SEARCH }

            R.string.settings_default_screen_want_to_visit -> { appSettings.defaultScreenRoute = SETTING_SCREEN_WANT_TO_VISIT }

            R.string.settings_default_font_size_small -> { appSettings.fontSize = SETTING_FONT_SIZE_SMALL }

            R.string.settings_default_font_size_large -> { appSettings.fontSize = SETTING_FONT_SIZE_LARGE }
        }

        return appSettings
    }

    fun saveAppSettingsToLocalStore(appSettings: AppSettings) {
        val newAppSettings = AppSettings(
            language = appSettings.language,
            darkTheme = appSettings.darkTheme,
            defaultScreenRoute = appSettings.defaultScreenRoute,
            fontSize = appSettings.fontSize,
            selectedOptionIDs = preferences.map { it.selectedOptionID }
        )

        val gson = Gson()
        val jsonString = gson.toJson(newAppSettings, AppSettings::class.java)

        launch {
            localStoreManager.saveAppSettingsToStore(jsonString)
            state.value = SettingsScreenUIState.UpdatedLocalStore
        }
    }

    // Updates only UI
    override fun updatePreference(nameID: Int, valueID: Int) {
        for (index in preferences.indices) {
            // Considering the fact that every setting has a unique name
            if (preferences[index].nameID == nameID) {
                preferences[index].selectedOptionID = valueID
                state.value = SettingsScreenUIState.PreferenceChanged
                break
            }
        }
    }
}
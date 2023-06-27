package cz.mendelu.pef.traveltravel.ui.screens.settings

sealed class SettingsScreenUIState {
    object Default : SettingsScreenUIState()
    object Success : SettingsScreenUIState()
    object PreferenceChanged : SettingsScreenUIState()
    object UpdatedLocalStore : SettingsScreenUIState()
}

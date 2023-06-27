package cz.mendelu.pef.traveltravel.ui.activities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import cz.mendelu.pef.traveltravel.architecture.BaseViewModel
import cz.mendelu.pef.traveltravel.localstore.LocalStoreManager
import cz.mendelu.pef.traveltravel.model.AppSettings
import kotlinx.coroutines.launch

class MainActivityViewModel(private val localStoreManager: LocalStoreManager)
    : BaseViewModel() {

    val appSettings: AppSettings = AppSettings()
    val state: MutableState<MainActivityUIState> = mutableStateOf(MainActivityUIState.Default)

    fun getAppSettings() {
        launch {
            localStoreManager.getAppSettingsFromStore().collect {
                if (it.isNotEmpty()) {
                    val gson = Gson()
                    val appSettings = gson.fromJson(it, AppSettings::class.java)

                    state.value = MainActivityUIState.Success(appSettings)
                }
            }
        }
    }
}
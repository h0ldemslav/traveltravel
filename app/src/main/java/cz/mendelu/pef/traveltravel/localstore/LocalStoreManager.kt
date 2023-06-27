package cz.mendelu.pef.traveltravel.localstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_cache")

class LocalStoreManager(private val context: Context) {
    companion object {
        val BUSINESS = stringPreferencesKey("business_record")
        val APP_SETTINGS = stringPreferencesKey("app_settings")
    }

    suspend fun saveAppSettingsToStore(jsonString: String) {
        context.dataStore.edit {
            it[APP_SETTINGS] = jsonString
        }
    }

    suspend fun saveTemporaryBusinessToStore(jsonString: String) {
        context.dataStore.edit {
            it[BUSINESS] = jsonString
        }
    }

    fun getBusinessRecordFromStore() = context.dataStore.data.map {
        it[BUSINESS] ?: ""
    }

    fun getAppSettingsFromStore() = context.dataStore.data.map {
        it[APP_SETTINGS] ?: ""
    }
}
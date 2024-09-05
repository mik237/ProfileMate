package me.ibrahim.profilemate.data.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.utils.AppConstants


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstants.APP_SETTINGS)


class LocalDataStoreManagerImpl(
    private val context: Context
) : LocalDataStoreManager {

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_TOKEN] = token
        }
    }

    override fun readToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_TOKEN] ?: ""
        }
    }

    override fun getToken(): String? = runBlocking {
        context.dataStore.data.map { it[PreferencesKeys.KEY_TOKEN] }.firstOrNull()
    }

}


private object PreferencesKeys {
    val KEY_TOKEN = stringPreferencesKey(AppConstants.KEY_TOKEN)
}
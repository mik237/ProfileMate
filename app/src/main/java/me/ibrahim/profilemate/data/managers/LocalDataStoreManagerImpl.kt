package me.ibrahim.profilemate.data.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import me.ibrahim.profilemate.domain.managers.LocalDataStoreManager
import me.ibrahim.profilemate.domain.models.User
import me.ibrahim.profilemate.utils.AppConstants
import me.ibrahim.profilemate.utils.fromJsonSafe
import me.ibrahim.profilemate.utils.toJsonSafe


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConstants.APP_SETTINGS)


class LocalDataStoreManagerImpl(
    private val context: Context,
    private val gson: Gson
) : LocalDataStoreManager {

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_TOKEN] = token
        }
    }

    override suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_USER] = gson.toJsonSafe(user)
        }
    }

    override fun readToken(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_TOKEN] ?: ""
        }
    }

    override fun readUser(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val userStr = preferences[PreferencesKeys.KEY_USER] ?: ""
            gson.fromJsonSafe(userStr, User::class.java)
        }
    }


    override suspend fun getUserId(): String {
        return readUser().firstOrNull()?.userId ?: ""
    }

    override fun getToken(): String? = runBlocking {
        context.dataStore.data.map { it[PreferencesKeys.KEY_TOKEN] }.firstOrNull()
    }

}


private object PreferencesKeys {
    val KEY_TOKEN = stringPreferencesKey(AppConstants.KEY_TOKEN)
    val KEY_USER = stringPreferencesKey(AppConstants.KEY_USER)
}
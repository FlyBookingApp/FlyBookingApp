package com.example.veyu.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val KEY_TOKEN = stringPreferencesKey("auth_token")
        val KEY_NAME = stringPreferencesKey("user_name")
        val KEY_EMAIL = stringPreferencesKey("user_email")
        val KEY_PHONE = stringPreferencesKey("user_phone")
    }

    // Lưu token
    suspend fun saveToken(token: String, name: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_NAME] = name
        }
    }

    suspend fun saveUserInfo(name: String, email: String, phone: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_EMAIL] = email
            prefs[KEY_PHONE] = phone
        }
    }

    // Xóa token
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_TOKEN)
            prefs.remove(KEY_NAME)
        }
    }

    // Lấy token
    val token: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_TOKEN] }

    val userName: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_NAME] }
}

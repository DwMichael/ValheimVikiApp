package com.rabbitv.valheimviki.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import com.rabbitv.valheimviki.utils.Constants.DEFAULT_LANG
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_DATA_LANGUAGE_KEY
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_GUIDED_ONBOARDING_STEP_KEY
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_KEY
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_LAST_SUCCESSFUL_DATA_REFRESH_AT_KEY
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_LANGUAGE_KEY
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_NAME
import com.rabbitv.valheimviki.utils.Constants.PREFERENCES_SETTINGS_TOOLTIP_STEP_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationsImpl(private val context: Context) : DataStoreOperations {
    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = PREFERENCES_KEY)
        val languageKey = stringPreferencesKey(name = PREFERENCES_LANGUAGE_KEY)
        val dataLanguageKey = stringPreferencesKey(name = PREFERENCES_DATA_LANGUAGE_KEY)
        val languagePopupKey = booleanPreferencesKey(name = com.rabbitv.valheimviki.utils.Constants.PREFERENCES_LANGUAGE_POPUP_KEY)
        val guidedOnboardingStepKey = stringPreferencesKey(name = PREFERENCES_GUIDED_ONBOARDING_STEP_KEY)
        val settingsTooltipStepKey = intPreferencesKey(name = PREFERENCES_SETTINGS_TOOLTIP_STEP_KEY)
        val lastSuccessfulDataRefreshAtKey = longPreferencesKey(name = PREFERENCES_LAST_SUCCESSFUL_DATA_REFRESH_AT_KEY)
    }

    private val dataStore = context.dataStore

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    override fun readOnBoardingState(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.onBoardingKey] ?: false
        }

    override suspend fun saveLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.languageKey] = language
        }
    }

    override fun languageProvider(): Flow<String> = dataStore.data
    .catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }
    .map { preferences ->
        preferences[PreferencesKey.languageKey] ?: DEFAULT_LANG
    }

    override suspend fun saveDataLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.dataLanguageKey] = language
        }
    }

    override fun dataLanguageProvider(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.dataLanguageKey] ?: DEFAULT_LANG
        }

    override suspend fun saveLanguagePopupState(shown: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.languagePopupKey] = shown
        }
    }

    override fun readLanguagePopupState(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.languagePopupKey] ?: false
        }

    override suspend fun saveGuidedOnboardingStep(step: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.guidedOnboardingStepKey] = step
        }
    }

    override fun readGuidedOnboardingStep(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.guidedOnboardingStepKey] ?: "INFO_CARD"
        }

    override suspend fun saveSettingsTooltipStep(step: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.settingsTooltipStepKey] = step
        }
    }

    override fun readSettingsTooltipStep(): Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.settingsTooltipStepKey] ?: 0
        }

    override suspend fun saveLastSuccessfulDataRefreshAt(timestampMillis: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.lastSuccessfulDataRefreshAtKey] = timestampMillis
        }
    }

    override fun readLastSuccessfulDataRefreshAt(): Flow<Long> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKey.lastSuccessfulDataRefreshAtKey] ?: 0L
        }
}

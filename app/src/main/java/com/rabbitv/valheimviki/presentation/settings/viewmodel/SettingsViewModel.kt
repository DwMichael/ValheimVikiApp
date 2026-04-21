package com.rabbitv.valheimviki.presentation.settings.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.rabbitv.valheimviki.domain.ads.AdManager
import com.rabbitv.valheimviki.domain.model.language.AppLanguage
import com.rabbitv.valheimviki.domain.use_cases.data_refetch.DataRefetchUseCase
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.presentation.settings.model.SettingsUiEvent
import com.rabbitv.valheimviki.presentation.settings.model.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreUseCases: DataStoreUseCases,
    private val dataRefetchUseCase: DataRefetchUseCase,
    val adManager: AdManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    /** Count language changes this session. Ad shown every [AD_THRESHOLD] switches. */
    private var languageChangeCount = 0

    init {
        observeLanguage()
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.LanguageSelected -> changeLanguage(event.language)
            is SettingsUiEvent.ShowLanguageDialog -> {
                _uiState.update { it.copy(showLanguageDialog = true) }
            }
            is SettingsUiEvent.DismissLanguageDialog -> {
                _uiState.update { it.copy(showLanguageDialog = false) }
            }
            is SettingsUiEvent.AdShown -> {
                _uiState.update { it.copy(showAdTrigger = false) }
            }
            is SettingsUiEvent.LanguageSwitchOverlayShown -> {
                _uiState.update { it.copy(isLanguageSwitching = false) }
            }
        }
    }

    /**
     * Show interstitial ad on given activity.
     * Called from Composable layer when [showAdTrigger] is true.
     */
    fun showAdIfNeeded(activity: Activity) {
        if (_uiState.value.showAdTrigger) {
            adManager.preloadAd()
            adManager.showAd(activity) {
                _uiState.update { it.copy(showAdTrigger = false) }
            }
        }
    }

    private fun observeLanguage() {
        dataStoreUseCases.languageProvider()
            .onEach { code ->
                _uiState.update { it.copy(currentLanguage = AppLanguage.fromCode(code)) }
            }
            .launchIn(viewModelScope)
    }

    private fun changeLanguage(language: AppLanguage) {
        val current = _uiState.value.currentLanguage
        if (language == current) {
            _uiState.update { it.copy(showLanguageDialog = false) }
            return
        }

        languageChangeCount++
        val shouldShowAd = languageChangeCount % AD_THRESHOLD == 0

        viewModelScope.launch {
            // 1) Start smooth switching overlay (avoids abrupt black flash)
            _uiState.update {
                it.copy(
                    isLanguageSwitching = true,
                    showLanguageDialog = false,
                )
            }

            delay(220)

            // 2) Save preference
            dataStoreUseCases.saveLanguageState(language.code)

            // 3) Update Android App Locale
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))

            // 4) Update UI
            _uiState.update {
                it.copy(
                    currentLanguage = language,
                    isRefetching = false,
                    showAdTrigger = shouldShowAd,
                )
            }
        }
    }

    companion object {
        /** Number of language changes before an interstitial ad is shown. */
        const val AD_THRESHOLD = 3
    }
}

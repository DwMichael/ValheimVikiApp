package com.rabbitv.valheimviki.data.repository

import com.rabbitv.valheimviki.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeDataStoreOperations @Inject constructor() : DataStoreOperations {
    private val _onboarding = MutableStateFlow(false)
    private val _language = MutableStateFlow("en")
    private val _popupShown = MutableStateFlow(false)
    private val _tooltipStep = MutableStateFlow(0)

    override suspend fun saveOnBoardingState(completed: Boolean) {
        _onboarding.value = completed
    }

    override fun readOnBoardingState(): Flow<Boolean> = _onboarding

    override suspend fun saveLanguage(language: String) {
        _language.value = language
    }

    override fun languageProvider(): Flow<String> = _language

    override suspend fun saveLanguagePopupState(shown: Boolean) {
        _popupShown.value = shown
    }

    override fun readLanguagePopupState(): Flow<Boolean> = _popupShown

    override suspend fun saveSettingsTooltipStep(step: Int) {
        _tooltipStep.value = step
    }

    override fun readSettingsTooltipStep(): Flow<Int> = _tooltipStep

    fun reset() {
        _onboarding.value = false
        _language.value = "en"
        _popupShown.value = false
        _tooltipStep.value = 0
    }
}

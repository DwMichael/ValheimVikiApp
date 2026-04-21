package com.rabbitv.valheimviki.presentation.settings.model

import com.rabbitv.valheimviki.domain.model.language.AppLanguage

data class SettingsUiState(
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val showLanguageDialog: Boolean = false,
    val isRefetching: Boolean = false,
    val isLanguageSwitching: Boolean = false,
    val showAdTrigger: Boolean = false,
)

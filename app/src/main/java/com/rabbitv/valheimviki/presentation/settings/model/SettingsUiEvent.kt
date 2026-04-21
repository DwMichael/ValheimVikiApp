package com.rabbitv.valheimviki.presentation.settings.model

import com.rabbitv.valheimviki.domain.model.language.AppLanguage

sealed class SettingsUiEvent {
    data class LanguageSelected(val language: AppLanguage) : SettingsUiEvent()
    data object DismissLanguageDialog : SettingsUiEvent()
    data object ShowLanguageDialog : SettingsUiEvent()
    data object AdShown : SettingsUiEvent()
    data object LanguageSwitchOverlayShown : SettingsUiEvent()
}

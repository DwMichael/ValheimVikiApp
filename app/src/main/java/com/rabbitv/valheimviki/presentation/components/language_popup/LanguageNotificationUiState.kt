package com.rabbitv.valheimviki.presentation.components.language_popup

data class LanguageNotificationUiState(
	val visible: Boolean = false,
	val secondsRemaining: Int = 6
) {
	val canDismiss: Boolean = secondsRemaining <= 0
}

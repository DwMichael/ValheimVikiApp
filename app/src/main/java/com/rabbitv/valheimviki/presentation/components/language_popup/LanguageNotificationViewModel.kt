package com.rabbitv.valheimviki.presentation.components.language_popup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class LanguageNotificationViewModel
@Inject
constructor(private val dataStoreUseCases: DataStoreUseCases) : ViewModel() {

    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup.asStateFlow()

    private val _canDismiss = MutableStateFlow(false)
    val canDismiss: StateFlow<Boolean> = _canDismiss.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(5)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _highlightSettings = MutableStateFlow(false)
    val highlightSettings: StateFlow<Boolean> = _highlightSettings.asStateFlow()

    init {
        checkIfShouldShowPopup()
    }

    private fun checkIfShouldShowPopup() {
        viewModelScope.launch {
            dataStoreUseCases.readOnBoardingUseCase().collect { isOnboardingCompleted ->
                val shown = dataStoreUseCases.readLanguagePopupState().first()
                if (isOnboardingCompleted && !shown && !_showPopup.value) {
                    _showPopup.value = true
                    startDismissTimer()
                }
            }
        }
    }

    private fun startDismissTimer() {
        viewModelScope.launch {
            for (i in 5 downTo 1) {
                _remainingSeconds.value = i
                delay(1000)
            }
            _remainingSeconds.value = 0
            _canDismiss.value = true
        }
    }

    fun dismissPopup() {
        if (_canDismiss.value) {
            viewModelScope.launch {
                dataStoreUseCases.saveLanguagePopupState(true)
                _showPopup.value = false

                // Show settings highlight after closing the popup
                _highlightSettings.value = true
                delay(3000)
                _highlightSettings.value = false
            }
        }
    }
}

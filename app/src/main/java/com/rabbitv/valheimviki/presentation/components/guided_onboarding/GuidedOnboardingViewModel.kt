package com.rabbitv.valheimviki.presentation.components.guided_onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuidedOnboardingViewModel @Inject constructor(
	private val dataStoreUseCases: DataStoreUseCases
) : ViewModel() {

	private val _uiState = MutableStateFlow(GuidedOnboardingUiState())
	val uiState: StateFlow<GuidedOnboardingUiState> = _uiState.asStateFlow()

	private var countdownJob: Job? = null

	init {
		viewModelScope.launch {
			val savedStep = dataStoreUseCases.readGuidedOnboardingStep().first()
			_uiState.update {
				it.copy(
					initialized = true,
					step = savedStep,
					secondsRemaining = if (savedStep == GuidedOnboardingStep.INFO_CARD) 6 else 0
				)
			}
			if (savedStep == GuidedOnboardingStep.INFO_CARD) {
				startInfoCountdown()
			}
		}
	}

	fun acceptInfoCard() {
		if (!_uiState.value.canAcceptInfoCard) return
		moveTo(GuidedOnboardingStep.HOME_SETTINGS)
	}

	fun onHomeSettingsTargetClicked() {
		if (_uiState.value.step != GuidedOnboardingStep.HOME_SETTINGS) return
		moveTo(GuidedOnboardingStep.SETTINGS_LANGUAGE)
	}

	fun nextSettingsStep() {
		when (_uiState.value.step) {
			GuidedOnboardingStep.SETTINGS_LANGUAGE -> moveTo(GuidedOnboardingStep.SETTINGS_FANDOM)
			GuidedOnboardingStep.SETTINGS_FANDOM -> moveTo(GuidedOnboardingStep.SETTINGS_DONATE)
			GuidedOnboardingStep.SETTINGS_DONATE -> moveTo(GuidedOnboardingStep.DONE)
			else -> Unit
		}
	}

	private fun moveTo(step: GuidedOnboardingStep) {
		countdownJob?.cancel()
		viewModelScope.launch {
			dataStoreUseCases.saveGuidedOnboardingStep(step)
			_uiState.update {
				it.copy(
					step = step,
					secondsRemaining = if (step == GuidedOnboardingStep.INFO_CARD) 6 else 0
				)
			}
			if (step == GuidedOnboardingStep.INFO_CARD) {
				startInfoCountdown()
			}
		}
	}

	private fun startInfoCountdown() {
		countdownJob?.cancel()
		countdownJob = viewModelScope.launch {
			while (_uiState.value.secondsRemaining > 0) {
				delay(1000)
				_uiState.update { state ->
					state.copy(secondsRemaining = (state.secondsRemaining - 1).coerceAtLeast(0))
				}
			}
		}
	}
}

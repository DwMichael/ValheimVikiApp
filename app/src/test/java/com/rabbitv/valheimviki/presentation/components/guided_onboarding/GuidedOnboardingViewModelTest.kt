package com.rabbitv.valheimviki.presentation.components.guided_onboarding

import com.rabbitv.valheimviki.domain.model.onboarding.GuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.data_language_provider.DataLanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_guided_onboarding_step.ReadGuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_language_popup_state.ReadLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_data_language_state.SaveDataLanguageState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_guided_onboarding_step.SaveGuidedOnboardingStep
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_language_popup_state.SaveLanguagePopupState
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GuidedOnboardingViewModelTest {

	private lateinit var testDispatcher: TestDispatcher
	private lateinit var readGuidedOnboardingStep: ReadGuidedOnboardingStep
	private lateinit var saveGuidedOnboardingStep: SaveGuidedOnboardingStep

	@BeforeEach
	fun setUp() {
		testDispatcher = StandardTestDispatcher()
		Dispatchers.setMain(testDispatcher)
		readGuidedOnboardingStep = mock()
		saveGuidedOnboardingStep = mock()
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun init_WithInfoCard_ShouldStartLockedInfoCard() = runTest(testDispatcher) {
		val viewModel = viewModel(initialStep = GuidedOnboardingStep.INFO_CARD)

		runCurrent()

		assertTrue(viewModel.uiState.value.initialized)
		assertEquals(GuidedOnboardingStep.INFO_CARD, viewModel.uiState.value.step)
		assertEquals(6, viewModel.uiState.value.secondsRemaining)
		assertFalse(viewModel.uiState.value.canAcceptInfoCard)
	}

	@Test
	fun acceptInfoCard_BeforeCountdownEnds_ShouldNotSaveNextStep() = runTest(testDispatcher) {
		val viewModel = viewModel(initialStep = GuidedOnboardingStep.INFO_CARD)
		runCurrent()

		viewModel.acceptInfoCard()
		runCurrent()

		assertEquals(GuidedOnboardingStep.INFO_CARD, viewModel.uiState.value.step)
		verify(saveGuidedOnboardingStep, never()).invoke(GuidedOnboardingStep.HOME_SETTINGS)
	}

	@Test
	fun acceptInfoCard_AfterCountdownEnds_ShouldMoveToHomeSettingsAndSave() = runTest(testDispatcher) {
		val viewModel = viewModel(initialStep = GuidedOnboardingStep.INFO_CARD)
		runCurrent()

		advanceTimeBy(6_000)
		runCurrent()
		viewModel.acceptInfoCard()
		runCurrent()

		assertEquals(GuidedOnboardingStep.HOME_SETTINGS, viewModel.uiState.value.step)
		verify(saveGuidedOnboardingStep).invoke(GuidedOnboardingStep.HOME_SETTINGS)
	}

	@Test
	fun init_WithSavedSettingsStep_ShouldResumeThatStepWithoutCountdown() = runTest(testDispatcher) {
		val viewModel = viewModel(initialStep = GuidedOnboardingStep.SETTINGS_FANDOM)

		runCurrent()

		assertTrue(viewModel.uiState.value.initialized)
		assertEquals(GuidedOnboardingStep.SETTINGS_FANDOM, viewModel.uiState.value.step)
		assertEquals(0, viewModel.uiState.value.secondsRemaining)
	}

	@Test
	fun nextSettingsStep_ShouldPersistEachSettingsStepUntilDone() = runTest(testDispatcher) {
		val viewModel = viewModel(initialStep = GuidedOnboardingStep.SETTINGS_LANGUAGE)
		runCurrent()

		viewModel.nextSettingsStep()
		runCurrent()
		assertEquals(GuidedOnboardingStep.SETTINGS_FANDOM, viewModel.uiState.value.step)

		viewModel.nextSettingsStep()
		runCurrent()
		assertEquals(GuidedOnboardingStep.SETTINGS_DONATE, viewModel.uiState.value.step)

		viewModel.nextSettingsStep()
		runCurrent()
		assertEquals(GuidedOnboardingStep.DONE, viewModel.uiState.value.step)

		verify(saveGuidedOnboardingStep).invoke(GuidedOnboardingStep.SETTINGS_FANDOM)
		verify(saveGuidedOnboardingStep).invoke(GuidedOnboardingStep.SETTINGS_DONATE)
		verify(saveGuidedOnboardingStep).invoke(GuidedOnboardingStep.DONE)
	}

	private fun viewModel(initialStep: GuidedOnboardingStep): GuidedOnboardingViewModel {
		whenever(readGuidedOnboardingStep()).thenReturn(flowOf(initialStep))
		return GuidedOnboardingViewModel(
			dataStoreUseCases = DataStoreUseCases(
				readOnBoardingUseCase = mock<ReadOnBoardingState>(),
				saveOnBoardingState = mock<SaveOnBoardingState>(),
				languageProvider = mock<LanguageProvider>(),
				saveLanguageState = mock<SaveLanguageState>(),
				dataLanguageProvider = mock<DataLanguageProvider>(),
				saveDataLanguageState = mock<SaveDataLanguageState>(),
				readLanguagePopupState = mock<ReadLanguagePopupState>(),
				saveLanguagePopupState = mock<SaveLanguagePopupState>(),
				readGuidedOnboardingStep = readGuidedOnboardingStep,
				saveGuidedOnboardingStep = saveGuidedOnboardingStep
			)
		)
	}
}

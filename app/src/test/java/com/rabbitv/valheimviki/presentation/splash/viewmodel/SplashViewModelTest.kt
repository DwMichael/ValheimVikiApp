package com.rabbitv.valheimviki.presentation.splash.viewmodel

import app.cash.turbine.test
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
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class SplashViewModelTest {

	private val testDispatcher = StandardTestDispatcher()


	@Mock
	private lateinit var dataStoreUseCases: DataStoreUseCases

	@Mock
	private lateinit var readOnBoardingUseCase: ReadOnBoardingState

	@Mock
	private lateinit var saveOnBoardingState: SaveOnBoardingState

	@Mock
	private lateinit var languageProvider: LanguageProvider

	@Mock
	private lateinit var saveLanguageState: SaveLanguageState

	@Mock
	private lateinit var dataLanguageProvider: DataLanguageProvider

	@Mock
	private lateinit var saveDataLanguageState: SaveDataLanguageState

	@Mock
	private lateinit var readLanguagePopupState: ReadLanguagePopupState

	@Mock
	private lateinit var saveLanguagePopupState: SaveLanguagePopupState

	@Mock
	private lateinit var readGuidedOnboardingStep: ReadGuidedOnboardingStep

	@Mock
	private lateinit var saveGuidedOnboardingStep: SaveGuidedOnboardingStep

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)

		dataStoreUseCases = DataStoreUseCases(
			readOnBoardingUseCase = readOnBoardingUseCase,
			saveOnBoardingState = saveOnBoardingState,
			languageProvider = languageProvider,
			saveLanguageState = saveLanguageState,
			dataLanguageProvider = dataLanguageProvider,
			saveDataLanguageState = saveDataLanguageState,
			readLanguagePopupState = readLanguagePopupState,
			saveLanguagePopupState = saveLanguagePopupState,
			readGuidedOnboardingStep = readGuidedOnboardingStep,
			saveGuidedOnboardingStep = saveGuidedOnboardingStep
		)

	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun onBoardingCompleted_InitLoad_ShouldBeFalse() {

		val viewModel = SplashViewModel(
			useCases = dataStoreUseCases,
			defaultDispatcher = testDispatcher
		)

		assertFalse(viewModel.onBoardingCompleted.value == true)
	}

	@Test
	fun onBoardingCompleted_UpdateValue_ShouldBeTrue() = runTest {
		whenever(readOnBoardingUseCase()).thenReturn(flowOf(true))

		val viewModel = SplashViewModel(
			DataStoreUseCases(
				readOnBoardingUseCase,
				saveOnBoardingState,
				languageProvider,
				saveLanguageState,
				dataLanguageProvider,
				saveDataLanguageState,
				readLanguagePopupState,
				saveLanguagePopupState,
				readGuidedOnboardingStep,
				saveGuidedOnboardingStep
			),
			testDispatcher
		)

		viewModel.onBoardingCompleted.test {
			assertFalse(awaitItem() == true)
			assertTrue(awaitItem() == true)
			cancelAndConsumeRemainingEvents()
		}
	}
}

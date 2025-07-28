package com.rabbitv.valheimviki.presentation.intro

import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import com.rabbitv.valheimviki.presentation.intro.viewmodel.WelcomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class WelcomeViewModelTest {

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

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		dataStoreUseCases = DataStoreUseCases(
			readOnBoardingUseCase = readOnBoardingUseCase,
			saveOnBoardingState = saveOnBoardingState,
			languageProvider = languageProvider,
			saveLanguageState = saveLanguageState
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `saveOnBoardingState should call use case with true parameter`() = runTest {
		// Given
		val viewModel = WelcomeViewModel(dataStoreUseCases)
		val completed = true

		// When
		viewModel.saveOnBoardingState(completed)

		// Then
		verify(saveOnBoardingState).invoke(completed = completed)
		verifyNoMoreInteractions(saveOnBoardingState)
	}

	@Test
	fun `saveOnBoardingState should call use case with false parameter`() = runTest {
		// Given
		val viewModel = WelcomeViewModel(dataStoreUseCases)
		val completed = false

		// When
		viewModel.saveOnBoardingState(completed)

		// Then
		verify(saveOnBoardingState).invoke(completed = completed)
		verifyNoMoreInteractions(saveOnBoardingState)
	}

	@Test
	fun `saveOnBoardingState should be called multiple times correctly`() = runTest {
		// Given
		val viewModel = WelcomeViewModel(dataStoreUseCases)

		// When
		viewModel.saveOnBoardingState(true)
		viewModel.saveOnBoardingState(false)
		viewModel.saveOnBoardingState(true)

		// Then
		verify(saveOnBoardingState, times(2)).invoke(completed = true)
		verify(saveOnBoardingState, times(1)).invoke(completed = false)
		verifyNoMoreInteractions(saveOnBoardingState)
	}

	@Test
	fun `saveOnBoardingState should not throw exception when called`() = runTest {
		// Given
		val viewModel = WelcomeViewModel(dataStoreUseCases)

		// When & Then
		val result = kotlin.runCatching {
			viewModel.saveOnBoardingState(true)
		}

		assertTrue(result.isSuccess)
	}

	@Test
	fun `WelcomeViewModel should be created successfully with dependencies`() {
		// Given & When
		WelcomeViewModel(dataStoreUseCases)

		// Then
		assertDoesNotThrow {
			// ViewModel should be created without throwing any exceptions
		}
	}
}
package com.rabbitv.valheimviki.presentation.splash.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.datastore.DataStoreUseCases
import com.rabbitv.valheimviki.domain.use_cases.datastore.get_onboarding_state.ReadOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.language_state_provider.LanguageProvider
import com.rabbitv.valheimviki.domain.use_cases.datastore.save_onboarding_state.SaveOnBoardingState
import com.rabbitv.valheimviki.domain.use_cases.datastore.saved_language_state.SaveLanguageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension


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


}
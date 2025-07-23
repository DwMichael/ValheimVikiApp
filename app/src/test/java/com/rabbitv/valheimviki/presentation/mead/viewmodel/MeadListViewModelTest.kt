package com.rabbitv.valheimviki.presentation.mead.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_mead_by_id.GetMeadByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_ids.GetMeadsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase
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
class MeadListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var meadUseCases: MeadUseCases

	@Mock
	private lateinit var getLocalMeadsUseCase: GetLocalMeadsUseCase

	@Mock
	private lateinit var getMeadByIdUseCase: GetMeadByIdUseCase

	@Mock
	private lateinit var getMeadsByIdsUseCase: GetMeadsByIdsUseCase

	@Mock
	private lateinit var getMeadsBySubCategoryUseCase: GetMeadsBySubCategoryUseCase

	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		meadUseCases = MeadUseCases(
			getLocalMeadsUseCase = getLocalMeadsUseCase,
			getMeadByIdUseCase = getMeadByIdUseCase,
			getMeadsByIdsUseCase = getMeadsByIdsUseCase,
			getMeadsBySubCategoryUseCase = getMeadsBySubCategoryUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

}
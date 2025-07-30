package com.rabbitv.valheimviki.presentation.mead.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.mead.Mead
import com.rabbitv.valheimviki.domain.model.mead.MeadSubCategory
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.mead.MeadUseCases
import com.rabbitv.valheimviki.domain.use_cases.mead.get_local_meads_use_case.GetLocalMeadsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_mead_by_id.GetMeadByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_ids.GetMeadsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.mead.get_meads_by_sub_category_use_case.GetMeadsBySubCategoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class MeadListViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

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
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))

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

	@Test
	fun meadListFlow_CategorySelected_ShouldEmitFilteredList() = runTest {
		val meadBases = listOf(
			Mead(
				id = Random.nextInt(0, 100).toString(),
				imageUrl = "",
				category = AppCategory.MEAD.toString(),
				subCategory = MeadSubCategory.MEAD_BASE.toString(),
				name = "H Mead Test Object",
				description = "",
				recipeOutput = "",
				effect = "",
				duration = "",
				cooldown = "",
				order = Random.nextInt(0, 100)
			),
			Mead(
				id = Random.nextInt(0, 100).toString(),
				imageUrl = "",
				category = AppCategory.MEAD.toString(),
				subCategory = MeadSubCategory.MEAD_BASE.toString(),
				name = "K Mead Test Object",
				description = "",
				recipeOutput = "",
				effect = "",
				duration = "",
				cooldown = "",
				order = Random.nextInt(0, 100)
			)
		)

		val potions = listOf(
			Mead(
				id = Random.nextInt(0, 100).toString(),
				imageUrl = "",
				category = AppCategory.MEAD.toString(),
				subCategory = MeadSubCategory.POTION.toString(),
				name = "A Mead Test Object",
				description = "",
				recipeOutput = "",
				effect = "",
				duration = "",
				cooldown = "",
				order = Random.nextInt(0, 100)
			)
		)
		val meads = meadBases + potions
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flowOf(meads))
		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)

		viewModel.meadList.test {
			val firstEmit = awaitItem()

			assertEquals(
				meadBases.sortedBy { it.order },
				firstEmit
			)
			assertEquals(2, firstEmit.size)
		}
	}

	@Test
	fun meadListFlow_CatchError_ShouldThrowError() = runTest {
		val errorMessage = "Test Exception from upstream"
		whenever(meadUseCases.getLocalMeadsUseCase()).thenReturn(flow {
			throw RuntimeException(
				errorMessage
			)
		})

		val viewModel = MeadListViewModel(
			meadUseCases = meadUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)

		viewModel.meadList.test {
			val error = awaitError()
			assertEquals(
				errorMessage,
				error.message,
				"The exception message should match the one thrown."
			)
		}
	}

}
package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import app.cash.turbine.test
import com.rabbitv.valheimviki.R.string.error_no_connection_with_empty_list_message
import com.rabbitv.valheimviki.domain.model.category.AppCategory
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterest
import com.rabbitv.valheimviki.domain.model.point_of_interest.PointOfInterestSubCategory
import com.rabbitv.valheimviki.domain.model.ui_state.uistate.UIState
import com.rabbitv.valheimviki.domain.repository.NetworkConnectivity
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_local_point_of_interest.GetLocalPointOfInterestUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id.GetPointOfInterestByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory.GetPointsOfInterestBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id.GetPointOfInterestBySubCategoryAndIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids.GetPointsOfInterestByIdsUseCase
import com.rabbitv.valheimviki.presentation.points_of_interest.model.PoiListUiEvent
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.fail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockitoExtension::class)
class PoiListViewModelTest {
	private val forsakenAltar = listOf(
		PointOfInterest(
			id = "10",
			imageUrl = "",
			category = AppCategory.POINTOFINTEREST.toString(),
			subCategory = PointOfInterestSubCategory.FORSAKEN_ALTAR.toString(),
			name = "H Poi Test Object",
			description = "",
			order = 1
		),
		PointOfInterest(
			id = "5",
			imageUrl = "",
			category = AppCategory.POINTOFINTEREST.toString(),
			subCategory = PointOfInterestSubCategory.FORSAKEN_ALTAR.toString(),
			name = "K Poi Test Object",
			description = "",
			order = 2
		)
	)

	private val structures = listOf(
		PointOfInterest(
			id = "3",
			imageUrl = "",
			category = AppCategory.POINTOFINTEREST.toString(),
			subCategory = PointOfInterestSubCategory.STRUCTURE.toString(),
			name = "A Poi Test Object",
			description = "",
			order = 1
		)
	)
	private val poiList = forsakenAltar + structures
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var connectivityObserver: NetworkConnectivity

	@Mock
	private lateinit var pointOfInterestUseCases: PointOfInterestUseCases

	@Mock
	private lateinit var getLocalPointOfInterestUseCase: GetLocalPointOfInterestUseCase

	@Mock
	private lateinit var getPointOfInterestByIdUseCase: GetPointOfInterestByIdUseCase

	@Mock
	private lateinit var getPointsOfInterestBySubCategoryUseCase: GetPointsOfInterestBySubCategoryUseCase

	@Mock
	private lateinit var getPointOfInterestBySubCategoryAndIdUseCase: GetPointOfInterestBySubCategoryAndIdUseCase

	@Mock
	private lateinit var getPointsOfInterestByIdsUseCase: GetPointsOfInterestByIdsUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
		pointOfInterestUseCases = PointOfInterestUseCases(
			getLocalPointOfInterestUseCase = getLocalPointOfInterestUseCase,
			getPointOfInterestByIdUseCase = getPointOfInterestByIdUseCase,
			getPointsOfInterestBySubCategoryUseCase = getPointsOfInterestBySubCategoryUseCase,
			getPointOfInterestBySubCategoryAndIdUseCase = getPointOfInterestBySubCategoryAndIdUseCase,
			getPointsOfInterestByIdsUseCase = getPointsOfInterestByIdsUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun filteredPoiListWithCategoryFlow_InitLoadCategory_ShouldBeForsakenAltar() =
		runTest {

			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					poiList
				)
			)

			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)


			viewModel.filteredPoiListWithCategory.test {
				val result = awaitItem()
				assertEquals(result.second, PointOfInterestSubCategory.FORSAKEN_ALTAR)
			}
		}

	@Test
	fun uiState_PoiUiStateInitLoad_EmitsLoadingState() = runTest {

		val viewModel = PoiListViewModel(
			pointOfInterestUseCases = pointOfInterestUseCases,
			connectivityObserver = connectivityObserver,
			defaultDispatcher = testDispatcher
		)


		viewModel.uiState.test {
			assert(awaitItem().poiListState is UIState.Loading)

		}
	}

	@Test
	fun uiState_PoiUiStateSuccessWithConnection_ShouldEmitSuccessWithPoiList() =
		runTest {
			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					poiList
				)
			)

			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				viewModel.onEvent(PoiListUiEvent.CategorySelected(PointOfInterestSubCategory.STRUCTURE))
				val result = awaitItem()

				when (result.poiListState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(
							PointOfInterestSubCategory.STRUCTURE,
							result.selectedSubCategory
						)
						assertEquals(
							structures.sortedBy { it.order },
							result.poiListState.data
						)
						assertEquals(1, result.poiListState.data.size)
					}
				}

			}
		}


	@Test
	fun uiState_PoiUiStateSuccessWithOutConnection_ShouldEmitSuccessWithPoiList() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					poiList
				)
			)

			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)


			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				val result = awaitItem()

				when (result.poiListState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(
							PointOfInterestSubCategory.FORSAKEN_ALTAR,
							result.selectedSubCategory
						)
						assertNotNull(result.selectedSubCategory)
						assertEquals(
							forsakenAltar.sortedBy { it.order },
							result.poiListState.data
						)
						assertEquals(2, result.poiListState.data.size)

					}
				}
			}
		}


	@Test
	fun uiState_PoiListUiStateEmptyWithConnection_RemainsLoadingState() =
		runTest {

			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					emptyList()
				)
			)
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				viewModel.onEvent(PoiListUiEvent.CategorySelected(PointOfInterestSubCategory.STRUCTURE))
				val result = awaitItem()

				when (result.poiListState) {
					is UIState.Loading -> {
						assertEquals(
							PointOfInterestSubCategory.STRUCTURE,
							result.selectedSubCategory
						)
					}

					is UIState.Success -> fail("Should not be success with empty poil")
					is UIState.Error -> fail("Should not be error when connected")
				}
			}
		}

	@Test
	fun uiState_MaterialsUiStateEmptyWithOutConnection_EmitErrorStateWithNoConnectionMessage() =
		runTest {
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(false))
			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					emptyList()
				)
			)

			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				val errorStateDueToNoConnection = awaitItem()

				when (errorStateDueToNoConnection.poiListState) {
					is UIState.Error -> {
						assertNotNull(errorStateDueToNoConnection.selectedSubCategory)
						assertEquals(
							error_no_connection_with_empty_list_message.toString(),
							errorStateDueToNoConnection.poiListState.message
						)
					}

					else -> fail("Initial state should transition to Error due to no connection. Actual: ${errorStateDueToNoConnection.poiListState}")
				}
			}
		}

	@Test
	fun uiState_MaterialsUiStateThrowError_EmitErrorState() =
		runTest {
			val errorMessage = "Failed to load materials from local source!"
			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(flow {
				throw RuntimeException(
					errorMessage
				)
			})

			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				val result = awaitItem()

				when (result.poiListState) {
					is UIState.Error -> {
						assertEquals(errorMessage, result.poiListState.message)
						assertNotNull(result.selectedSubCategory)
					}

					is UIState.Loading -> fail("State should have transitioned from Loading to Error State")
					is UIState.Success -> fail("State should emit error state")
				}
			}
		}


	@Test
	fun uiState_MaterialsUiStateCategoryChanged_ShouldChangedCategory() =
		runTest {

			whenever(pointOfInterestUseCases.getLocalPointOfInterestUseCase()).thenReturn(
				flowOf(
					poiList
				)
			)
			whenever(connectivityObserver.isConnected).thenReturn(flowOf(true))
			val viewModel = PoiListViewModel(
				pointOfInterestUseCases = pointOfInterestUseCases,
				connectivityObserver = connectivityObserver,
				defaultDispatcher = testDispatcher
			)

			viewModel.uiState.test {
				val initLoad = awaitItem()
				assert(initLoad.poiListState is UIState.Loading)

				val forsakenAltarsResult = awaitItem()

				assert(forsakenAltarsResult.poiListState is UIState.Success)
				assertNotNull(forsakenAltarsResult.selectedSubCategory)
				assertEquals(
					PointOfInterestSubCategory.FORSAKEN_ALTAR,
					forsakenAltarsResult.selectedSubCategory
				)
				assertEquals(2, (forsakenAltarsResult.poiListState as UIState.Success).data.size)

				viewModel.onEvent(PoiListUiEvent.CategorySelected(PointOfInterestSubCategory.STRUCTURE))
				val structureResults = awaitItem()

				when (structureResults.poiListState) {
					is UIState.Error -> fail("State should not emit error")
					is UIState.Loading -> fail("State should have transitioned from Loading to Success")
					is UIState.Success -> {
						assertEquals(
							PointOfInterestSubCategory.STRUCTURE,
							structureResults.selectedSubCategory
						)
						val expectedPoilData = structures.sortedBy { it.order }
						assertEquals(expectedPoilData, structureResults.poiListState.data)
						assertEquals(1, structureResults.poiListState.data.size)
					}
				}
			}
		}

}
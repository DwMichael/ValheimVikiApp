package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_local_point_of_interest.GetLocalPointOfInterestUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_id.GetPointOfInterestByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory.GetPointsOfInterestBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interest_by_subcategory_and_id.GetPointOfInterestBySubCategoryAndIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.get_point_of_interests_by_ids.GetPointsOfInterestByIdsUseCase
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
class PoiListViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

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


}
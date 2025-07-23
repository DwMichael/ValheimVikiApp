package com.rabbitv.valheimviki.presentation.points_of_interest.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.point_of_interest.PointOfInterestUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock

class PoiListViewModelTest {
	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var pointOfInterestUseCases: PointOfInterestUseCases

	@Mock
	private lateinit var getLocalOreDepositsUseCase: GetLocalOreDepositUseCase

	@Mock
	private lateinit var getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase

	@Mock
	private lateinit var getOreDepositByIdUseCase: GetOreDepositByIdUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		pointOfInterestUseCases = PointOfInterestUseCases(
			getLocalPointOfInterestUseCase = TODO(),
			getPointOfInterestByIdUseCase = TODO(),
			getPointsOfInterestBySubCategoryUseCase = TODO(),
			getPointOfInterestBySubCategoryAndIdUseCase = TODO(),
			getPointsOfInterestByIdsUseCase = TODO()
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


}
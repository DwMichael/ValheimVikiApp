package com.rabbitv.valheimviki.presentation.ore_deposit.viewmodel

import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.OreDepositUseCases
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
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
class OreDepositScreenViewModelTest {

	private val testDispatcher = StandardTestDispatcher()

	@Mock
	private lateinit var oreDepositUseCases: OreDepositUseCases

	@Mock
	private lateinit var getLocalOreDepositsUseCase: GetLocalOreDepositUseCase

	@Mock
	private lateinit var getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase

	@Mock
	private lateinit var getOreDepositByIdUseCase: GetOreDepositByIdUseCase


	@BeforeEach
	fun setUp() {
		Dispatchers.setMain(testDispatcher)
		oreDepositUseCases = OreDepositUseCases(
			getLocalOreDepositsUseCase = getLocalOreDepositsUseCase,
			getOreDepositsByIdsUseCase = getOreDepositsByIdsUseCase,
			getOreDepositByIdUseCase = getOreDepositByIdUseCase
		)
	}

	@AfterEach
	fun tearDown() {
		Dispatchers.resetMain()
	}


}
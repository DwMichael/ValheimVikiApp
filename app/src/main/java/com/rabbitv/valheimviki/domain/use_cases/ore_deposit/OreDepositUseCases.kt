package com.rabbitv.valheimviki.domain.use_cases.ore_deposit

import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class OreDepositUseCases @Inject constructor(
	val getLocalOreDepositsUseCase: GetLocalOreDepositUseCase,
	val getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase,
	val getOreDepositByIdUseCase: GetOreDepositByIdUseCase,

	)
package com.rabbitv.valheimviki.domain.use_cases.ore_deposit

import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit.GetLocalOreDepositUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id.GetOreDepositByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids.GetOreDepositsByIdsUseCase

data class OreDepositUseCases(
    val getLocalOreDepositsUseCase: GetLocalOreDepositUseCase,
    val getOreDepositsByIdsUseCase: GetOreDepositsByIdsUseCase,
    val getOreDepositByIdUseCase: GetOreDepositByIdUseCase,

    )
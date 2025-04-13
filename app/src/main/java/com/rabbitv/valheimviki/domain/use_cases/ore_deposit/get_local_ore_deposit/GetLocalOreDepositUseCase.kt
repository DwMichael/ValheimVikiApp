package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetLocalOreDepositUseCase @Inject constructor(
    private val repository: OreDepositRepository
) {
    operator fun invoke():Flow<List<OreDeposit>>{
        return repository.getLocalOreDeposits()
    }
}
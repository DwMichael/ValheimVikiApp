package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_local_ore_deposit

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetLocalOreDepositUseCase @Inject constructor(
    private val repository: OreDepositRepository
) {
    operator fun invoke(): Flow<List<OreDeposit>> {
        return repository.getLocalOreDeposits()
            .map { oreDeposits -> oreDeposits.sortedBy { it.order } }.flowOn(Dispatchers.IO)
    }
}
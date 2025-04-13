package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.insert_ore_deposit

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject

class InsertOreDepositUseCase @Inject constructor(
    private val repository: OreDepositRepository
) {
    suspend operator fun invoke(oreDeposits: List<OreDeposit>){
        return repository.insertOreDeposit(oreDeposits)
    }
}
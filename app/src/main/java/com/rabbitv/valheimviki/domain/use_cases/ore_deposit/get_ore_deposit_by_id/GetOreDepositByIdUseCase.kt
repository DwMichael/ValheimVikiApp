package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id

import com.rabbitv.valheimviki.domain.exceptions.OreDepositByIdFetchLocalException
import com.rabbitv.valheimviki.domain.exceptions.OreDepositsByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject

class GetOreDepositByIdUseCase @Inject constructor(
    private val repository: OreDepositRepository
) {
    operator fun invoke(id:String): OreDeposit? {
        return try {
             repository.getOreDepositById(id) ?:throw OreDepositByIdFetchLocalException("Ore Deposit with id $id not found")
        }catch (e : Exception)
        {
            throw OreDepositsByIdsFetchLocalException("Error fetching from Room Ore deposit by id : ${e.message}")
        }
    }
}
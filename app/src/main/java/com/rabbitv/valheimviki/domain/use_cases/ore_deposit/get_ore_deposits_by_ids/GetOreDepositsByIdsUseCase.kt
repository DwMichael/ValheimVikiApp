package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids

import com.rabbitv.valheimviki.domain.exceptions.OreDepositsByIdsFetchLocalException
import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject


class GetOreDepositsByIdsUseCase @Inject constructor(
    private val repository: OreDepositRepository
) {
    operator fun invoke(ids:List<String>):List<OreDeposit>{
        return try {
            val oreDeposit = repository.getOreDepositsByIds(ids)
            if (oreDeposit.isNotEmpty()) {
                 oreDeposit
            }else
            {
                throw OreDepositsByIdsFetchLocalException("No ore deposits found with ids $ids")
            }
        } catch (e: Exception) {
            throw OreDepositsByIdsFetchLocalException("Error fetching from Room ore deposits by ids : ${e.message}")
        }
    }
}
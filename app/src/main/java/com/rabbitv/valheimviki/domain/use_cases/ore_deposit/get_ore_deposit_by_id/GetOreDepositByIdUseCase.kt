package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposit_by_id

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetOreDepositByIdUseCase @Inject constructor(
	private val repository: OreDepositRepository
) {
	operator fun invoke(id: String): Flow<OreDeposit?> = repository.getOreDepositById(id)

}
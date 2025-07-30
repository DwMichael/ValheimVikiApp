package com.rabbitv.valheimviki.domain.use_cases.ore_deposit.get_ore_deposits_by_ids

import com.rabbitv.valheimviki.domain.model.ore_deposit.OreDeposit
import com.rabbitv.valheimviki.domain.repository.OreDepositRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


class GetOreDepositsByIdsUseCase @Inject constructor(
	private val repository: OreDepositRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<OreDeposit>> =
		repository.getOreDepositsByIds(ids)

}
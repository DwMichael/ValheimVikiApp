package com.rabbitv.valheimviki.domain.use_cases.armor.get_armors_by_ids

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArmorsByIdsUseCase @Inject constructor(
	private val armorRepository: ArmorRepository
) {
	operator fun invoke(ids: List<String>): Flow<List<Armor>> = armorRepository.getArmorsByIds(ids)
}
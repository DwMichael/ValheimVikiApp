package com.rabbitv.valheimviki.domain.use_cases.armor.get_armor_by_id

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArmorByIdUseCase @Inject constructor(
	private val armorRepository: ArmorRepository
) {
	operator fun invoke(id: String): Flow<Armor?> = armorRepository.getArmorById(id)
}
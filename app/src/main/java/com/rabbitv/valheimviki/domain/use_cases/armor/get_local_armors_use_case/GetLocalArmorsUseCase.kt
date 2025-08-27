package com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case

import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalArmorsUseCase @Inject constructor(
	private val armorRepository: ArmorRepository
) {
	operator fun invoke(): Flow<List<Armor>> = armorRepository.getLocalArmors()

}
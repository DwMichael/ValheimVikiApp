package com.rabbitv.valheimviki.domain.use_cases.armor.get_local_armors_use_case

import com.rabbitv.valheimviki.domain.exceptions.ArmorFetchLocalException
import com.rabbitv.valheimviki.domain.model.armor.Armor
import com.rabbitv.valheimviki.domain.repository.ArmorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalArmorsUseCase @Inject constructor(
    private val armorRepository: ArmorRepository
) {
    operator fun invoke(): Flow<List<Armor>> {
        return try {
            armorRepository.getLocalArmors().map { armors ->
                armors.sortedBy { it.order }
            }
        } catch (e: Exception) {
            throw ArmorFetchLocalException("Get local Armors encounter exception $e")
        }

    }
}
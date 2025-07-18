package com.rabbitv.valheimviki.domain.use_cases.weapon.get_local_weapons_use_case

import com.rabbitv.valheimviki.domain.exceptions.WeaponFetchLocalException
import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalWeaponsUseCase @Inject constructor(
	private val weaponRepository: WeaponRepository
) {
	operator fun invoke(): Flow<List<Weapon>> {
		return try {
			weaponRepository.getLocalWeapons().map { weapon ->
				weapon.sortedBy { it.order }
			}
		} catch (e: Exception) {
			throw WeaponFetchLocalException("Get local Weapons encounter exception $e")
		}

	}
}
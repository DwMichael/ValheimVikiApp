package com.rabbitv.valheimviki.domain.use_cases.weapon.get_local_weapons_use_case

import com.rabbitv.valheimviki.domain.model.weapon.Weapon
import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalWeaponsUseCase @Inject constructor(
	private val weaponRepository: WeaponRepository
) {
	operator fun invoke(): Flow<List<Weapon>> = weaponRepository.getLocalWeapons()

}
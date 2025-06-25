package com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_ids

import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import javax.inject.Inject

class GetWeaponsByIdsUseCase @Inject constructor(
	private val _weaponRepository: WeaponRepository
) {
	operator fun invoke(ids: List<String>) = _weaponRepository.getWeaponsByIds(ids)
}
package com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapon_by_id_use_case

import com.rabbitv.valheimviki.domain.repository.WeaponRepository
import javax.inject.Inject

class GetWeaponByIdUseCase @Inject constructor(
    private val repository: WeaponRepository
) {
    operator fun invoke(id: String) = repository.getWeaponById(id)
}
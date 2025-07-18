package com.rabbitv.valheimviki.domain.use_cases.weapon

import com.rabbitv.valheimviki.domain.use_cases.weapon.get_local_weapons_use_case.GetLocalWeaponsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapon_by_id_use_case.GetWeaponByIdUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_ids.GetWeaponsByIdsUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_category_use_case.GetWeaponsBySubCategoryUseCase
import com.rabbitv.valheimviki.domain.use_cases.weapon.get_weapons_by_sub_type_use_case.GetWeaponsBySubTypeUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
data class WeaponUseCases @Inject constructor(
	val getLocalWeaponsUseCase: GetLocalWeaponsUseCase,
	val getWeaponsByIdsUseCase: GetWeaponsByIdsUseCase,
	val getWeaponByIdUseCase: GetWeaponByIdUseCase,
	val getWeaponsBySubCategoryUseCase: GetWeaponsBySubCategoryUseCase,
	val getWeaponsBySubTypeUseCase: GetWeaponsBySubTypeUseCase,
)
